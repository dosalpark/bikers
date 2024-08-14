# Bikers
Bike 관리 및 커뮤니티 어플리케이션

## 주요 기능
* Bike 관리기능 제공
* 카카오톡, Naver 로그인 기능 제공
* 주행일지 작성 기능 제공
* 환경검사 일정 전 알림 제공
* 채팅방 생성 및 실시간 채팅기능 제공
* 게시글, 댓글을 통한 커뮤니티 기능 제공
* 이미지 업로드 기능 제공

## Stacks
* Back-End
  - Java
  - SpringBoot
  - Gradle
* 데이터베이스
  - MySQL : 메인 데이터베이스
  - Redis : 캐싱 (RefreshToken, Mail인증)
* 인증 및 권한
  - Jwt
  - OAuth : Kakao, Naver
* 실시간 통신
  - WebSocket
  - STOMP

## 목표 (지속적으로 추가)
* Jwt AccessToken, RefreshToken 적용 (완료)
* post, comment, BikeModel, Bike CRUD (완료)
* 바이크 관리(주행거리 등록, 환경검사 알림등) (완료)
* 채팅기능 구현 및 Stomp 적용 (완료)
* 투어 등록시 로그인시 등록 위치 기반으로 투어에 대한 알림메세지 전송

## 적용

<details>
<summary>Spring Event를 사용한 도메인 간 의존성 완화 <a href="https://pshistory.tistory.com/90" target="_blank">[블로그]</a></summary>
<div markdown="1">       
  
  ### 도입이유
  
  Bike의 mileage(키로수)가 변경 되었을 때 전/후 Mileage와 이동경로 등 히스토리를 저장하기 위해 BikeMileage 도메인 생성  
  (이동경로는 추후 사용으로 당장은 전/후 Mileage 만 저장)
  
  당장 하나의 서비스를 주입받는 것은 문제가 없지만 Bikers의 코어 도메인인 Bike에서 서비스가 확대됨에 따라  
  의존성을 지속적으로 주입 받으면 Bike 도메인의 로직에만 집중할 수 없을것같다고 판단하여 Spring Event 도입
  

  ![Event가 발생했을때 전달할 Class](https://github.com/dosalpark/bikers/assets/154612223/39935ee1-65ef-4cab-bd34-00df334c7fe0)
  
  해당 클래스를 통해서 Event 발생시 값 전달
  
  ![Bike Mileage 변경 메소드](https://github.com/dosalpark/bikers/assets/154612223/8f3b05b2-37c6-4156-b758-2d251f894c22)
  
  Bike의 Mileage가 변경되었을때 Spring Event를 통해 Event Class를 전달

  ![Event 전달 받은 후 저장](https://github.com/dosalpark/bikers/assets/154612223/78140772-741f-456c-9738-118a95a89130)
  
  BikeMileage는 Event Class를 전달받아서 repository에 저장


  ### @TransactionalEventListner, @Transactional(propagation = Propagation.REQUIRES_NEW)
  
  무조건 저장하는게 아닌 bikeService의 updateMyBikeMileage 메소드의 트랜잭션이 정상적으로 commit 된 후 BikeMileage에도 저장되어야 한다고 판단하여
  @EventListener 를 사용하는게 아닌 @TransactionalEventListner(phase = TransactionPhase.AFTER_COMMIT) 옵션을 사용

  또한 bikeMileageService의 addMileageHistory 메소드도 데이터를 저장해야 하므로 @Transactional 적용 하였으나 @TransactionalEventListner 와 같이 사용하기 위해서는 
  새로운 트랜잭션을 생성하거나, 트랜잭션을 적용하지 않는  propagation 옵션을 사용해야 하여 새로운 트랜잭션을 생성하는 REQUIRES_NEW 옵션을 적용 함


  아래의 순서로 실행됨
  1. BikeService.updateMyBikeMileage -> bike객체 mileage 업데이트
  2. BikeService.updateMyBikeMileage -> bike객체 저장<br>
  --- updateMyBikeMileage 의 트랜잭션이 정상적으로 commit 된 후 ---
  3. BikeMileageService.addMileageHistory  -> bikeMileage 객체 생성
  4. BikeMileageService.addMileageHistory  -> bikeMileage 객체 저장
  
</div>
</details>

<details>
<summary>메일 인증 구현 <a href="https://pshistory.tistory.com/92" target="_blank">[블로그]</a></summary>
<div markdown="1">
  
  ### 적용이유

  아무렇게나 계정을 생성하는 것을 막기위해서 email 인증을 도입
  도입함으로서 본인임을 인증하고 사용하기에 악성 댓글등이 감소하는 효과를 기대
  
  동작방식
  1. email을 body에 담아 요청
  2. 해당 email에 인증코드 발송
  3. email과 인증코드를 같이 입력
  * exception: 이메일 형식이 맞지 않을 때, 인증코드가 맞지않을 때, 인증코드 유효시간이 지났을 때

  ![인증메일 발송](https://github.com/dosalpark/bikers/assets/154612223/ca92bfda-1a03-46f4-83ad-a54fdd827060)

  email을 기재하고 요청을 보냄

  ![인증번호 확인](https://github.com/dosalpark/bikers/assets/154612223/6f70d024-ba61-4a4a-a645-2f836171c206)

  email에 도착한 메일에 인증번호 확인

  ![인증번호 입력](https://github.com/dosalpark/bikers/assets/154612223/34382a00-0d87-4cde-b311-52ee7832b537)

  email과 인증번호를 같이 입력하면 Http Status 204로 성공, DB에서 해당 내용 삭제

  ![잘못된 인증번호 입력](https://github.com/dosalpark/bikers/assets/154612223/1d09f49d-3643-48c4-a9c1-3b4e0cf6a37a)

  잘못된 인증번호 입력하면 exception 발생

  ![인증시간 만료](https://github.com/dosalpark/bikers/assets/154612223/85ddc73c-28dc-4315-8ed3-fe28a924b7c2)

  설정한 인증시간이 지나게되면 exception 발생

</div>
</details>

<details>
<summary>Jwt RefreshToken 적용 <a href="https://pshistory.tistory.com/93" target="_blank">[블로그]</a></summary>
<div markdown="1">  
  
  ### 적용이유
  
  AccessToken의 만료시간을 길게 줄 경우 탈취되었을때 보안상의 위험에 대비해서 RefreshToken 적용

  ![로그인](https://github.com/dosalpark/bikers/assets/154612223/8a3be9c9-a03f-468c-b8d5-58b9e288cc67)

  로그인을 하게 되면 Response Header에 AccessToken과 RefreshToken 전달

  ![AccessToken 만료, RefreshToken 전달안함](https://github.com/dosalpark/bikers/assets/154612223/dd560fad-cd4c-458b-8f3c-61d06435666e)

  AccessToken이 만료되고 RefreshToken을 전달하지 않으면 HttpStatus 400과 오류메세지 전달

  ![AccessToken 만료, RefreshToken 전달](https://github.com/dosalpark/bikers/assets/154612223/881cc1d3-3761-42ab-8bc8-dcba534230f4)

  AccessToken이 만료되었을때 RefreshToken을 같이 전달하면 새로운 AccessToken이 전달됨

  ![RefreshToken 만료](https://github.com/dosalpark/bikers/assets/154612223/7c94aca5-c381-413f-865c-f4637ab7886d)
  ![AccessToken 만료, RefreshToken 만료](https://github.com/dosalpark/bikers/assets/154612223/6024b340-9216-4bb5-ae27-e8075834588d)

  등록한 RefreshToken이 TTL이 지나서 만료되었을때 RefreshToken을 전달하더라도 HttpStatus 400과 오류메세지를 전달


  ### 정리
  
  1. 인가 필요한 요청시 Access Token/Refresh Token 만료 -> 토큰만료, 리프레시토큰 없습니다(실패)
  2. 인가 필요한 요청시 Access Token만료/Refresh Token 없음 -> 토큰만료, 리프레시토큰 없습니다(실패)
  3. 인가 필요한 요청시 Access Token만료/Refresh Token 만료되지 않음 -> Access Token 발급

  RefreshToken을 사용함으로서 AccessToken의 수명을 짧게 가져갈 수 있어서 보안성을 높힐 수 있었고, 사용자가 자주 로그인하지 않아도 되면서 사용자 경험이 크게 개선되었다고 생각


</div>
</details>

<details>
<summary>Jwt RefreshToken 수정 <a href="https://pshistory.tistory.com/94" target="_blank">[블로그]</a></summary>
<div markdown="1">  

  ### 수정이유
  Redis에 RefreshToken을 저장할 때 변동성이 있는 RefreshToken을 key값으로 사용하여 사용자가 같은 ID로 계속 로그인 한다면 기존 RefreshToken이 갱신되는게 아니라 Redis에 새로운 RefreshToken이 저장됨

  기존 value인 변경되지 않는 member의 PK, email을 조합해서 memberInfo를 생성해서 key로 사용하고 RefreshToken을 value로 저장
  ```
  //JwtTokenProvider.java
//기존
 public String createRefreshToken(Long userId, String email) {
        ...
        redisTemplate.opsForValue().set(
            createRefreshToken,
            userId + ":" + email,
            Duration.ofMillis(refreshTokenExpireMilliSecond));

        return BEARER_PREFIX + createRefreshToken;
    }
        
//변경
public String createRefreshToken(Long userId, String email) {
        ...
        redisTemplate.opsForValue().set(
            userId + ":" + email,
            createRefreshToken,
            Duration.ofMillis(refreshTokenExpireMilliSecond));

        return BEARER_PREFIX + createRefreshToken;
    }
  ```

  AuthrizationFilter에서는 ExpiredJwtException에서 getClaims()를 통해서 만료된 AccessToken의 Claims를 받아오고 Claims에 있는 memberInfo를 통해서 RefreshToken을 검증하고 새로운 AccessToken을 만들어서 Response Header에 전달하도록 수정
  ```
//AuthorizationFilter.java
//기존
	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
    	FilterChain filterChain) throws ServletException, IOException {
        ...
        } catch (ExpiredJwtException e) {
            String refreshToken = jwtTokenProvider.getRefreshTokenFromHeader(request);
            if (StringUtils.hasText(refreshToken)) {
                String memberInfo = jwtTokenProvider.getMemberInfoFromRefreshToken(refreshToken);
                if (StringUtils.hasText(memberInfo)) {
                    Long memberId = Long.valueOf(memberInfo.split(":")[0]);
                    String email = memberInfo.split(":")[1];

                    String newAccessToken = jwtTokenProvider.createAccessToken(memberId, email);
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    response.setContentType("application/json; charset=UTF-8");
                    response.addHeader(JwtTokenProvider.AUTHORIZATION_HEADER, newAccessToken);
                    response.getWriter().write(new ObjectMapper().writeValueAsString(
                        CommonResponseDto.success("200", "새로운 토큰이 발급되었습니다.")));
                    return;
                }
            }
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(
                CommonResponseDto.fail("400", "토큰 만료 및 리프레시 토큰이 없습니다. 다시 로그인 해주세요.")));
            return;
        }
		...
    }

//변경
	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        ...
        } catch (ExpiredJwtException e) {
            String refreshToken = jwtTokenProvider.getRefreshTokenFromHeader(request);
            if (StringUtils.hasText(refreshToken)) {
                Claims info = e.getClaims();
                Long memberId = info.get("userId", Long.class);
                String email = info.get("email", String.class);
                String memberInfo = memberId + ":" + email;
                if (jwtTokenProvider.validateRefreshToken(memberInfo)) {
                    String newAccessToken = jwtTokenProvider.createAccessToken(memberId, email);
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    response.setContentType("application/json; charset=UTF-8");
                    response.addHeader(JwtTokenProvider.AUTHORIZATION_HEADER, newAccessToken);
                    response.getWriter().write(new ObjectMapper().writeValueAsString(
                        CommonResponseDto.success("200", "새로운 토큰이 발급되었습니다.")));
                    return;
                }
            }
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(
                CommonResponseDto.fail("400", "토큰 만료 및 리프레시 토큰이 없습니다. 다시 로그인 해주세요.")));
            return;
        }
        ...
    }
  ```

</div>
</details>

<details>
<summary>QueryDsl + 검색기능 적용 <a href="https://pshistory.tistory.com/60" target="_blank">[블로그_QueryDsl]</a> / <a href="https://pshistory.tistory.com/91" target="_blank">[블로그_검색기능]</a></summary>
<div markdown="1">  
  
   ### 적용이유
   JPQL로 작성했을때의 오탈자 체크 및 타입체크의 귀찮음이 발생해 오탈자, 타입체크를 IDE에서 지원하는 QueryDsl 적용

   (IntelliJ CE 버전 사용으로 쿼리메소드 작성에도 약간 귀찮음이 있어서 도입한 이유도 있음..)

   다른이용자의 Bike를 확인 할 때 원하는 조건의 Bike만 확인 하고 싶을수도 있다는 판단하에 BooleanExpression을 통해 검색기능 적용

   ```
	// BikeRepositoryImpl.java
 	// QueryDsl 적용 전, 검색기능 적용 전 

	@Override
    	public Slice<BikesGetResponseDto> findAllPagable(Pageable pageable) {
        	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	
	        Order order = pageable.getSort().stream().findFirst().orElse(null);
	        String orderBuild = " ORDER BY b." + order.getProperty() + " " + order.getDirection();

	        TypedQuery<jakarta.persistence.Tuple> query = entityManager.createQuery(
	            "SELECT b.id, " 
		         + "b.memberId, " 
		         + "bm.manufacturer, " 
		         + "bm.name, " 
		         + "bm.year, " 
		         + "bm.bikeCategory, " 
		         + "bm.displacement, " 
		         + "b.nickName, " 
		         + "b.status, " 
		         + "b.createdAt " 
		         + "FROM Bike b LEFT JOIN BikeModel bm ON b.bikeModelId = bm.id " 
		         + "WHERE b.status != 'DELETE' AND b.visibility = true "
	                + orderBuild, jakarta.persistence.Tuple.class);
	        query.setFirstResult((int) pageable.getOffset());
	        query.setMaxResults(pageable.getPageSize() + 1);
	
	        List<jakarta.persistence.Tuple> result = query.getResultList();
	        boolean hasNext = result.size() == pageable.getPageSize() + 1;
	
	        if (hasNext) {
	            result.remove(pageable.getPageSize());
	        }
	        Slice<jakarta.persistence.Tuple> a = new SliceImpl<>(result, pageable, hasNext);
	        return conveterToDtoSlice(a);
    	}

   ```	
  QueryDsl 도입 전에는 문자열로 쿼리를 직접 작성해야하므로 실행해서 런타임 오류로만 쿼리가 잘못됬는지 확인이 필요했음

  ```
	// BikeRepositoryImpl.java
 	// QueryDsl 적용 후, 검색기능 적용 후

	@Override
	public Slice<BikesGetResponseDto> getBikes(Pageable pageable, String name, String manufacturer,
        Integer year, String email, String status) {
	        List<BikesGetResponseDto> getBikes = queryFactory.select(
	                Projections.constructor(BikesGetResponseDto.class,
	                    bike.id,
	                    member.email,
	                    bikeModel.manufacturer,
	                    bikeModel.name,
	                    bikeModel.year,
	                    bikeModel.bikeCategory,
	                    bikeModel.displacement,
	                    bike.nickName,
	                    bike.status,
	                    bike.createdAt)
	            ).from(bike)
	            .leftJoin(bikeModel).on(bike.bikeModelId.eq(bikeModel.id))
	            .leftJoin(member).on(bike.memberId.eq(member.id))
	            .where(
	                bike.visibility.eq(true),
	                statusNe(status),
	                bikeModelNameEq(name),
	                manufacturerEq(manufacturer),
	                bikeModelYearEq(year),
	                ownerEmailEq(email)
	            )
	            .offset(pageable.getOffset())
	            .limit(pageable.getPageSize() + 1)
	            .orderBy(getOrder(pageable))
	            .fetch();
	
	        boolean hasNext = getBikes.size() == pageable.getPageSize() + 1;
	
	        if (hasNext) {
	            getBikes.remove(pageable.getPageSize());
	        }
	        return new SliceImpl<>(getBikes, pageable, hasNext);
  	}
  ```
  QueryDsl 도입 후에는 코드를 통해서 쿼리를 작성하기에 컴파일 단계에서 오탈자, 타입오류 체크가 가능해짐

  (QueryDsl 또한 JPA를 이용하는 방식으로 실행속도는 크게 달라지진 않음)

  이전에는 Tuple로 반환받아서 Dto로 변환작업을 했었는데 불필요한 단계를 줄이고자 생성자로 Projections 적용해서 속도 및 불필요한 코드를 줄임
  
  방법에는 생성자와 필드, Bean이 있는데 Bean 방식은 Dto에 Setter를 적용해야 하며, 필드는 필드명이 다르다면 as()를 통해서 맞춰줘야 함
  
  생성자는 순서만 맞추면되서 생성자 방식으로 적용 

  <a href="https://pshistory.tistory.com/61" target="_blank">[Projection 관련 작성글]


  ```
	private OrderSpecifier<?> getOrder(Pageable pageable) {
	        Sort.Order order = pageable.getSort().get().findFirst().orElse(null);
	        Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
	
	        PathBuilder<BikeModel> path = new PathBuilder<>(BikeModel.class, "bikeModel");
	        DateTimePath<LocalDateTime> dateTimePath;
	
	        switch (order.getProperty()) {
	            case "createdAt":
	                dateTimePath = path.getDateTime("createdAt", LocalDateTime.class);
	                return new OrderSpecifier<>(direction, dateTimePath);
	            case "modifiedAt":
	                dateTimePath = path.getDateTime("modifiedAt", LocalDateTime.class);
	                return new OrderSpecifier<>(direction, dateTimePath);
	            default:
	                throw new IllegalArgumentException("정렬기준이 정확하지 않습니다");
	        }
	    }
  ```
  정렬기능은 위와 같이 리턴값이 OrderSpecifier인 getOrder 메소드를 생성해서 입력되는 값에 따라서 다르게 정렬 할 수 있도록 설정

  ```
	private BooleanExpression bikeModelNameEq(String name) {
	        return StringUtils.hasText(name) ? bikeModel.name.eq(name) : null;
	}	
	
	private BooleanExpression manufacturerEq(String manufacturer) {
		return StringUtils.hasText(manufacturer) ?
	    	bikeModel.manufacturer.eq(Manufacturer.valueOf(manufacturer.toUpperCase())) : null;
	}
	
	private BooleanExpression bikeModelYearEq(Integer year) {
		return year != null ? bikeModel.year.eq(year) : null;
	}

	private BooleanExpression ownerEmailEq(String email) {
	        return StringUtils.hasText(email) ? member.email.eq(email) : null;
    	}
	
    	private Predicate statusNe(String bikeStatus) {
	        return StringUtils.hasText(bikeStatus) ?
    		bike.status.ne(BikeStatus.valueOf(bikeStatus)) : null;
    	}
  ```

  검색값은 Controller에서 require = false 옵션으로 받아서 위와 같은 메소드를 거쳐서 이용자가 입력했다면 BooleanExpression을 where절에 추가하고 입력하지 않았다면 null값으로 입력해서 하나의 쿼리를 동적으로 사용 할 수 있도록 설정
  

</div>
</details>

<details>
<summary>웹소켓을 이용한 채팅기능 구현 <a href="https://pshistory.tistory.com/96" target="_blank">[블로그]</a></summary>
<div markdown="1">  
  
   ### 도입이유
   번개나 정기모임등 오토바이 투어등록시 일정 등 관련한 대화를 나눌수있도록 어플리케이션 내에 채팅기능 도입

   아직 별도의 프론트가 구축되어있지 않아서 접근하는 uri인 ws://localhost:8080/ws/talk 뒤에 쿼리파람방식으로 참가 할 채팅방의 id값을 'RoomId=' 로 이용자의 AccessToken을 'Autorization=' 로 입력해서 handshake 전에 uri를 파싱해서 검증 후 웹소켓 채팅방에 접근 가능하도록 설정
   

   ```
	// WebsocketConfig.java

	@Configuration  
	@EnableWebSocket  
	@RequiredArgsConstructor  
	public class WebsocketConfig implements WebSocketConfigurer {  
	  
	    private final WebSocketHandler webSocketHandler;  
	    private final HttpSessionHandshakeInterceptor httpSessionHandshakeInterceptor;  
	  
	    @Override  
	    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {  
	        registry.addHandler(webSocketHandler, "/ws/talk")  
	            .addInterceptors(httpSessionHandshakeInterceptor)  
	            .setAllowedOrigins("*");  
	    }  
	}

   ```	
  uri를 파싱하고 검증 할 수 있도록 addInterceptors 옵션 추가

  ```
	// CustomHandshakeInterceptor.java

	@Component  
	@RequiredArgsConstructor  
	public class CustomHandshakeInterceptor extends HttpSessionHandshakeInterceptor {  
	  
	    private final JwtTokenProvider jwtTokenProvider;  
	    private final TalkRoomMemberService talkRoomMemberService;  
	  
	    @Override  
	    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,  
	        WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {  
	        Long memberId, roomId;  
	        String accessToken, email;  
	        try {  
	            accessToken = jwtTokenProvider.getAccessTokenFromRequest(request);  
	            Claims memberInfo = jwtTokenProvider.getUserInfoFromAccessToken(accessToken);  
	            memberId = memberInfo.get("userId", Long.class);  
	            email = memberInfo.get("email", String.class);  
	            roomId = getRoomIdFromRequest(request);  
	  
	            talkRoomMemberService.validateMemberInTalkRoom(roomId, memberId);  
	        } catch (SecurityException | MalformedJwtException | SignatureException |  
	                 ExpiredJwtException | NotFoundException e) {  
	            return false;  
	        }        
	        attributes.put("memberId",memberId);  
	        attributes.put("email", email);  
	        attributes.put("roomId", roomId);  
	  
	        return true;  
	    }  
	    private Long getRoomIdFromRequest(ServerHttpRequest request) {  
	        String query = request.getURI().getQuery();  
	        if (query != null && query.contains("RoomId=")) {  
	            String roomId = query.split("RoomId=")[1];  
	            return Long.parseLong(roomId.split("&")[0]);  
	        }        return null;  
	    }  
	}
  ```
  JwtTokenProvider와 TalkRoomMemberService 클래스를 이용해서 토큰이 정상인지 해당 회원이 채팅방에 가입된 회원인지 확인


  ```
	// WebsocketTalkHandler.java

	    ...
	    @Override  
	    protected void handleTextMessage(WebSocketSession session, TextMessage message)  
	        throws Exception {  
	        String getPayload = message.getPayload();  
	        sendMessage(session, getPayload, false);  
	    }  
	    
	    private void sendMessage(WebSocketSession session, String msg,  
	        boolean isConnectionEstablished) {  
	        String memberId = session.getAttributes().get("memberId").toString();  
	        String email = session.getAttributes().get("email").toString();  
	        String roomId = session.getAttributes().get("roomId").toString();  
	        TextMessage textMessage;  
	        if (isConnectionEstablished) {  
	            textMessage = new TextMessage(email + msg);  
	        } else {  
	            textMessage = new TextMessage(email + " : " + msg);  
	        }        sessionSet.parallelStream().forEach(otherSession -> {  
	            try {  
	                String otherSessionRoomId = otherSession.getAttributes().get("roomId").toString();  
	                if (otherSession.isOpen()  
	                    && roomId.equals(otherSessionRoomId)) {  
	                    otherSession.sendMessage(textMessage);  
	                }            
	            } catch (IOException e) {  
	                throw new RuntimeException(e);  
	            }        
	        });  
	        publisher.publishEvent(  
	            new TalkAutoSaveEventDto(Long.parseLong(roomId), Long.parseLong(memberId), msg));  
	    }  
	    ...
  ```
  handshake 이후 웹소켓세션이 생기면 메세지를 발송할 때 인터셉터에서 웹소켓세션에 추가한 email을 통해서 발송자명을 설정하고 roomId를 통해서 세션안에 들어있는 이용자들중 roomId가 동일한 회원에게만 메세지 발송

  memberId는 메세지를 저장할 때 사용하며 메세지 저장은 Spring Event를 이용하여 TalkHistoryService로 이벤트 발생시킴

  ```
	// TalkHistoryService.java
	
	...
	    @Transactional
	    @EventListener
	    public void wsTalkAutoSave(TalkAutoSaveEventDto talkAutoSaveEventDto) {
	        Long roomId = talkAutoSaveEventDto.getRoomId();
	        Long memberId = talkAutoSaveEventDto.getMemberId();
	        String msg = talkAutoSaveEventDto.getMsg();
	
	        TalkHistory talk = new TalkHistory(roomId, memberId, msg);
	        talkHistoryRepository.save(talk);
	    }
	...
  ```
  TalkHistoryService에서는 값들을 전달받아서 DB에 채팅내용을 저장함

  **같은 채팅방 일 때**
  
  ![같은방](https://github.com/user-attachments/assets/cd390069-2b30-4f14-8ec8-bcff3acb3261)

  **다른 채팅방 일 때**
  
  ![다른방](https://github.com/user-attachments/assets/feab0217-278b-44ab-a426-a49ffcf5a45b)

</div>
</details>

<details>
<summary>채팅기능 Stomp 적용 <a href="https://pshistory.tistory.com/97" target="_blank">[블로그]</a></summary>
<div markdown="1">  
  
   ### 도입이유
   WebSocketSeesion을 통해서 메세지를 전송하는데 백엔드에서 WebSocketSession 객체를 생성할 수 없으며,

   기존에는 쿼리파람형식으로 accessToken을 노출시켜 보안상 취약하다고 생각
   
   ```
	//WebsocketConfig.java

	@Configuration
	@EnableWebSocketMessageBroker
	@RequiredArgsConstructor
	public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {
	
	    private final ChannelInterceptor channelInterceptor;
	
	    @Override
	    public void registerStompEndpoints(StompEndpointRegistry registry) {
	        registry.addEndpoint("/ws/talk")
	            .setAllowedOrigins("*");
	    }
	
	    @Override
	    public void configureMessageBroker(MessageBrokerRegistry registry) {
	        registry.enableSimpleBroker("/sub");
	        registry.setApplicationDestinationPrefixes("/pub");
	    }
	
	    @Override
	    public void configureClientInboundChannel(ChannelRegistration registration) {
	        registration.interceptors(channelInterceptor);
	    }
	
	}
   ```
   WebSocketConfigurer 대신 MessageBorker를 이용해서 메세지를 전달하기 떄문에 WebSocketMessageBrokerConfigurer를 상속받음
   
   각각의 메소드는 연결 엔트포인트설정, 발송/구독 url의 prefix 설정, 통신간 header를 인터셉터할 수 있도록 설정 하는 역할을 가지고있음

   ```
	//CustomChannelInterceptor.java
	
	@Slf4j
	@Component
	@RequiredArgsConstructor
	public class CustomChannelInterceptor implements ChannelInterceptor {
	
	    private final JwtTokenProvider jwtTokenProvider;
	    private final TalkRoomMemberService talkRoomMemberService;
	
	    @Override
	    public Message<?> preSend(Message<?> message, MessageChannel channel) {
	        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
	
	        if (StompCommand.CONNECT == accessor.getCommand()) {
	            String accessToken = Objects.requireNonNull(
	                accessor.getFirstNativeHeader("Authorization")).substring(7);
	            Long roomId = Long.valueOf(
	                Objects.requireNonNull(accessor.getFirstNativeHeader("RoomId")));
	
	            jwtTokenProvider.validateToken(accessToken);
	            Claims memberInfo = jwtTokenProvider.getUserInfoFromAccessToken(accessToken);
	            Long memberId = memberInfo.get("userId", Long.class);
	            talkRoomMemberService.validateMemberInTalkRoom(roomId, memberId);
	        }
	
	        return message;
	    }
	}
   ```
   ChannelInterceptor를 상속받아서 통신 연결시 header를 확인해서 accessToken 검증 및 해당 사용자가 채팅방에 소속되어있는지 확인

   ```
	//TalkController.java
	
	    ...
	    @MessageMapping("/talk-rooms/{roomId}")
	    public void sendTalk(TalkDto talkDto, @DestinationVariable Long roomId) {
	        talkService.sendTalk(
	            talkDto.getAccessToken(),
	            talkDto.getMsg(),
	            roomId);
	    }
	    ...
	
	//TalkService.java
	
	    ...
	    public void sendTalk(String accessToken, String msg, Long roomId) {
	        Claims memberInfo = jwtTokenProvider.getUserInfoFromAccessToken(accessToken.substring(7));
	        Long sendMemberId = memberInfo.get("userId", Long.class);
	        String sendMemberEmail = memberInfo.get("email", String.class);
	
	        messagingTemplate.convertAndSend("/sub/talk-rooms/" + roomId,
	            sendMemberEmail + " : " + msg);
	
	        publisher.publishEvent(
	            new TalkAutoSaveEventDto(roomId, sendMemberId, msg));
	    }
	    ...
   ```
   사용하는 메세지 발송 Url은 '/pub/talk-rooms/{roomId}' 로 roomId를 Url에 포함시켜서 전송

   @MessageMapping 어노테이션을 통하여 /pub/talk-rooms/{roomId}로 들어오는 요청을 해당 컨트롤러에서 처리

   @MessageMapping이 설정된 메소드는 @PathVariable을 사용할 수 없어서 @DestinationVariable 을 통해서 roomId를 받아옴

   roomId를 topic으로 설정하여 발송시 해당 roomId를 구독하고있는 사용자에게만 메세지 전달 후 TalkHistory 객체로 생성해서 DB에 저장


   ```
	//1. TalkRoomMemberService.java
	
	    @Transactional
	    public void joinRoom(Long memberId, String memberEmail, Long roomId) {
	        TalkRoom getRoom = talkRoomService.findByTalkRoom(roomId);
	        if (talkRoomMemberRepository.existsByRoomIdAndJoinMemberId(roomId, memberId)) {
	            throw new IllegalArgumentException("이미 들어가있는 톡방입니다.");
	        }
	        TalkRoomMember joinTalkRoom = new TalkRoomMember(getRoom.getId(), memberId);
	        talkRoomMemberRepository.save(joinTalkRoom);
	
	        publisher.publishEvent(new JoinTalkRoomSendEventDto(roomId, memberId, memberEmail));
	    }
	
	//2. TalkService.java
	
	    @Transactional(propagation = Propagation.REQUIRES_NEW)
	    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	    public void JoinNoticeSendTalk(JoinTalkRoomSendEventDto joinTalkRoomSendEventDto) {
	        String msg = joinTalkRoomSendEventDto.getMemberEmail() + " 님이 채팅방에 들어왔습니다.";
	        messagingTemplate.convertAndSend("/sub/talk-rooms/" + joinTalkRoomSendEventDto.getRoomId(),
	            msg);
	
	        publisher.publishEvent(
	            new JoinTalkRoomHistoryEventDto(joinTalkRoomSendEventDto.getRoomId(),
	                joinTalkRoomSendEventDto.getMemberId(), msg));
	    }
	    
	//3. TalkHistoryService.java
	
	    @Transactional(propagation = Propagation.REQUIRES_NEW)
	    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	    public void joinTalkRoomNotice(JoinTalkRoomHistoryEventDto joinTalkRoomHistoryEventDto) {
	        Long roomId = joinTalkRoomHistoryEventDto.getRoomId();
	        Long memberId = joinTalkRoomHistoryEventDto.getMemberId();
	        String msg = joinTalkRoomHistoryEventDto.getMsg();
	
	        TalkHistory talk = new TalkHistory(roomId, memberId, msg);
	        talkHistoryRepository.save(talk);
	    }
   ```
   채팅방에 입장 했을때 입장메세지를 전달하는 방법은 위의 코드에 작성된 순번대로 처리되며 채팅방에서 나갔을 때도 동일한 방식으로 처리

   단계간 Spring Event 방식을 이용하여 처리를 진행

   1. 사용자가 최초 채팅방에 참여하게되면 talk_room_members 테이블에 저장

   2. 참여한 채팅방의 roomId를 구독하고 있는 사용자에게 해당 이용자가 채팅방에 입장했다는 메세지를 발송

   3. 채팅방에 입장했다는 메세지를 talk_history 테이블에 저장

   ![입장](https://github.com/user-attachments/assets/50b32bc6-b567-4d55-85e3-fb6228cb9f86)

   ![나가기](https://github.com/user-attachments/assets/e74844c1-8eb7-470d-8b8e-4c15945d185f)

   
</div>
</details>
