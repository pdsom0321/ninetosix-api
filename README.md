# ninetosix-api 

## Environment
+ Java 17
+ Gradle
+ Spring Boot 2.7.1
+ JPA
+ jwt
+ Swagger

## Main Features
#### [Member]
#### [Attend]
 
## Deployment
#### [Automated CI/CD Setup with GitHub Action]
<pre><code>1. 소스 코드는 GitHub에 저장
2. GitHub Action을 설정하여 코드 변경이 발생할 때마다 자동으로 빌드와 테스트 수행
3. 빌드와 테스트가 성공적으로 완료되면, 빌드된 애플리케이션 파일은 AWS S3에 업로드
4. AWS EC2 인스턴스에 CodeDeploy 에이전트가 설치되어 있으며, S3에 업로드된 애플리케이션 파일은 CodeDeploy를 통해 배포
5. CodeDeploy는 배포 중 애플리케이션의 상태를 모니터링하며, 배포가 성공적으로 완료되면 애플리케이션을 자동으로 시작</code></pre>

## Convention
#### [commit convention]
<pre><code>Add : 추가
Modify : 수정
Fix : 버그 수정
Refactor : 리팩토링, 기능은 그대로 두고 코드를 수정
Style : formatting, 세미콜론 추가 / 코드 변경은 없음
Test : 테스트 추가, 서버 설정 관련 테스트
Rename : 파일 혹은 폴더명을 수정하거나 옮기는 작업만인 경우
Remove : 파일을 삭제하는 작업만 수행한 경우
Chore : 라이브러리 설치, 빌드 작업 업데이트
Comment	: 필요한 주석 추가 및 변경
Docs : 문서 변경</code></pre>
