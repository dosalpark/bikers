# Bikers

Bike manage application

## 목표 (지속적으로 추가)
* Jwt AccessToken, RefreshToken 적용 (완료)
* post, comment, BikeModel, Bike CRUD (완료)
* 바이크 관리(주행거리 등록, 환경검사 등)
* 채팅기능 구현
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
