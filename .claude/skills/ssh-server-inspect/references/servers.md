# SSH 접속 환경 설정

> 이 파일을 수정하면 스킬 전체에 반영됩니다. SKILL.md는 건드리지 않아도 됩니다.
> 실제 키 파일은 글로벌 스킬(`~/.claude/skills/ssh-server-inspect/keys/`)에 보관합니다.

## PEM 키 파일

키 파일은 글로벌 스킬 `keys/` 디렉토리에 보관한다.

| 용도 | 파일명 | Windows 경로 |
|------|--------|-------------|
| 운영 (Bastion + 프라이빗 EC2) | `guhada_prod.pem` | `%USERPROFILE%\.claude\skills\ssh-server-inspect\keys\guhada_prod.pem` |
| QA 서버 | `guhada.pem` | `%USERPROFILE%\.claude\skills\ssh-server-inspect\keys\guhada.pem` |

SSH 명령에서 사용할 경로:
- **운영**: `C:\Users\youn.wonhee\.claude\skills\ssh-server-inspect\keys\guhada_prod.pem`
- **QA**: `C:\Users\youn.wonhee\.claude\skills\ssh-server-inspect\keys\guhada.pem`

## Bastion 서버 (mgmt)

| 항목 | 값 |
|------|-----|
| 호스트 | `ec2-52-79-112-173.ap-northeast-2.compute.amazonaws.com` |
| 내부 IP | `172.30.0.189` |
| 호스트명 | `mgmt` |
| 유저 | `ec2-user` |
| OS | Amazon Linux 2 |

## 단축 별칭

> 스킬 인자로 아래 단축어를 입력하면 해당 도메인으로 자동 매핑한다.

| 단축어 | 실제 도메인 | 비고 |
|--------|------------|------|
| `pe` | `product-engine7` | product-engine7이 기본 PE |

## 운영 서버 목록

> 접속 방식: Bastion `go <도메인명>` 경유 (방식 B)
> 출처: Bastion `/etc/hosts`

| 도메인명 | 내부 IP | 용도 |
|---------|---------|------|
| batch | 172.30.0.149 | 배치 서버 |
| benefit1 | 172.30.0.106 | 혜택 서버 1 |
| benefit2 | 172.30.0.128 | 혜택 서버 2 |
| gateway | 172.30.0.116 | 게이트웨이 1 |
| gateway2 | 172.30.0.4 | 게이트웨이 2 |
| notification1 | 172.30.0.141 | 알림 서버 1 |
| notification2 | 172.30.0.9 | 알림 서버 2 |
| ecs-notification | 172.30.1.171 | ECS 알림 |
| order1 | 172.30.0.8 | 주문 서버 1 |
| order2 | 172.30.0.138 | 주문 서버 2 |
| product1 | 172.30.0.34 | 상품 서버 1 |
| product2 | 172.30.0.24 | 상품 서버 2 |
| community1 | 172.30.0.34 | 커뮤니티 1 (product1과 동일 IP) |
| community2 | 172.30.0.24 | 커뮤니티 2 (product2와 동일 IP) |
| product-engine | 172.30.1.105 | 상품 엔진 1 |
| product-engine2 | 172.30.1.56 | 상품 엔진 2 |
| product-engine3 | 172.30.0.192 | 상품 엔진 3 |
| product-engine4 | 172.30.0.131 | 상품 엔진 4 |
| product-engine5 | 172.30.0.16 | 상품 엔진 5 |
| product-engine7 | 172.30.0.115 | 상품 엔진 7 |
| product-engine8 | 172.30.0.160 | 상품 엔진 8 |
| product-engine9 | 172.30.0.56 | 상품 엔진 9 |
| search1 | 172.30.0.102 | 검색 서버 1 |
| search2 | 172.30.0.239 | 검색 서버 2 |
| admin1 | 172.30.0.98 | 어드민 1 |
| admin2 | 172.30.0.36 | 어드민 2 |
| user1 | 172.30.0.215 | 유저 서버 1 |
| user2 | 172.30.0.45 | 유저 서버 2 |
| claim1 | 172.30.0.215 | 클레임 서버 1 (user1과 동일 IP) |
| claim2 | 172.30.0.45 | 클레임 서버 2 (user2와 동일 IP) |
| ship | 172.30.0.101 | 배송 서버 1 |
| ship2 | 172.30.0.129 | 배송 서버 2 |
| delivery1 | 172.30.0.101 | 배송 서버 1 (ship과 동일 IP) |
| delivery2 | 172.30.0.129 | 배송 서버 2 (ship2와 동일 IP) |
| settle1 | 172.30.0.105 | 정산 서버 1 |
| settle2 | 172.30.0.76 | 정산 서버 2 |
| redis | 172.30.0.249 | Redis |
| blockchain | 172.30.0.235 | 블록체인 |
| temco | 172.30.0.62 | Temco 서버 |
| web | 172.30.0.204 | 웹 서버 1 |
| web2 | 172.30.0.145 | 웹 서버 2 |
| ai | 172.30.0.158 | AI 서버 |
| elastic | 172.30.0.248 | Elasticsearch |
| kglowing-elastic | 172.30.0.46 | kglowing Elasticsearch (방식 D ProxyCommand) |
| kglowing-batch | 172.30.0.171 | kglowing 배치 서버 |
| gaiaapi | 172.30.0.251 | Gaia API |
| apm | 172.30.0.86 | APM 서버 |

## QA 서버 목록

> 접속 방식: 직접 접속 (방식 A, QA PEM 키 사용)

| 별칭 | 호스트 | 용도 |
|------|--------|------|
| qa-settle, settle | ec2-13-125-228-131.ap-northeast-2.compute.amazonaws.com | QA settle |
| qa-product, product | ec2-13-125-186-53.ap-northeast-2.compute.amazonaws.com | QA 상품 |
| qa-user, user | ec2-13-125-84-91.ap-northeast-2.compute.amazonaws.com | QA 유저 |
| qa-claim, claim | ec2-13-125-84-91.ap-northeast-2.compute.amazonaws.com | QA 클레임 (user와 동일) |
| qa-search, search | ec2-13-125-33-63.ap-northeast-2.compute.amazonaws.com | QA 검색 |
| qa-order, order | ec2-54-180-132-179.ap-northeast-2.compute.amazonaws.com | QA 주문 |
| qa-benefit, benefit | ec2-3-34-127-49.ap-northeast-2.compute.amazonaws.com | QA 혜택 |
| qa-seller-admin, seller-admin | ec2-13-125-24-136.ap-northeast-2.compute.amazonaws.com | QA 셀러 어드민 |
| qa-batch, batch | ec2-3-36-120-192.ap-northeast-2.compute.amazonaws.com | QA 배치 |
| qa-shipping, shipping | ec2-52-78-80-187.ap-northeast-2.compute.amazonaws.com | QA 배송 |
| qa-product-engine, product-engine | ec2-54-180-123-193.ap-northeast-2.compute.amazonaws.com | QA 상품 엔진 |

## 서버별 메모

### kglowing-batch (172.30.0.171)
- OS: Amazon Linux 2023
- Java: 17 (Amazon Corretto)
- 앱 로그: `/home/guhada/logs/{서비스명}/`
- 실행 중인 서비스 (4개):
  - `kglowing-amazon-list` — 로그: `/home/guhada/logs/kglowing-amazon-list/`
  - `kglowing-elastic-batch` — 로그: `/home/guhada/logs/kglowing-elastic-batch/`
  - `kglowing-creator-analysis` — 로그: `/home/guhada/logs/kglowing-creator-analysis/`
  - `kglowing_tiktok` — 로그: `/home/guhada/logs/kglowing_tiktok/kglowing_tiktok-error.log`

### kglowing-elastic (172.30.0.46)
- OS: Amazon Linux 2023
- ES 버전: 8.12 (RPM, systemd 관리)
- RAM: 16GB
- ES 로그: `/var/log/elasticsearch/` (elasticsearch 유저 소유 → ProxyCommand 필요)
- ES 설정: `/etc/elasticsearch/`

### qa-settle (ec2-13-125-228-131)
- OS: Amazon Linux 2
- RAM: 1.9GB
- 앱 로그: `/home/ec2-user/logs/` (ec2-user 소유)
- 주요 로그 파일: `settle.log`, `settle-error.log`
