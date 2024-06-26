# Bikers

<details>
<summary>Spring Event를 사용한 도메인 간 의존성 완화</summary>
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
