# 쉘 스크립트 (Shell Script)

> `[2] 입문` · 선수 지식: [자동화란](./what-is-automation.md)

> 쉘 명령어를 파일에 저장하여 순차적으로 실행하는 스크립트 프로그램

`#ShellScript` `#쉘스크립트` `#Bash` `#배시` `#Shell` `#쉘` `#CLI` `#CommandLine` `#터미널` `#Terminal` `#Linux` `#Unix` `#PowerShell` `#파워쉘` `#스크립팅` `#Scripting` `#자동화` `#Automation` `#Cron` `#크론` `#Environment` `#환경변수` `#Pipeline` `#파이프라인` `#Shebang` `#시뱅`

## 왜 알아야 하는가?

쉘 스크립트는 시스템 관리, 배포, 자동화의 기본 도구입니다. 서버 설정, 배치 작업, CI/CD 파이프라인 등 다양한 곳에서 사용됩니다. 간단한 자동화는 Python보다 쉘 스크립트가 더 빠르고 가볍습니다.

## 핵심 개념

- **Shebang**: 스크립트 실행 인터프리터 지정 (`#!/bin/bash`)
- **변수**: 값 저장 및 참조 (`$VAR`)
- **조건문**: if-else로 분기 처리
- **반복문**: for, while로 반복 실행
- **파이프라인**: 명령어 출력을 다음 명령어 입력으로 연결 (`|`)

## 쉽게 이해하기

**쉘 스크립트**를 요리 레시피에 비유할 수 있습니다.

```
요리사(사용자)가 매번 직접 요리 vs 레시피(스크립트)로 자동화

수동:
1. 재료 준비 (직접)
2. 썰기 (직접)
3. 볶기 (직접)

자동화 (쉘 스크립트):
┌─────────────────────────────┐
│  recipe.sh (레시피 파일)    │
│  ───────────────────────    │
│  prepare_ingredients        │
│  cut_vegetables             │
│  stir_fry                   │
└─────────────────────────────┘
        ↓
    ./recipe.sh 실행
        ↓
   전체 과정 자동 수행
```

## 상세 설명

### 기본 구조

```bash
#!/bin/bash
# Shebang: 이 스크립트를 bash로 실행

# 주석은 # 으로 시작

# 변수 정의 (= 주변에 공백 없이!)
NAME="World"

# 변수 사용
echo "Hello, $NAME!"

# 명령어 실행 결과 저장
TODAY=$(date +%Y-%m-%d)
echo "오늘 날짜: $TODAY"
```

### 변수

```bash
# 변수 할당 (공백 주의!)
VAR="value"      # ✅ 올바름
VAR = "value"    # ❌ 오류

# 변수 참조
echo $VAR        # 단순 참조
echo ${VAR}      # 명시적 참조 (권장)
echo "${VAR}_suffix"  # 문자열 연결 시

# 특수 변수
$0    # 스크립트 이름
$1    # 첫 번째 인자
$#    # 인자 개수
$@    # 모든 인자
$?    # 이전 명령어 종료 코드
$$    # 현재 프로세스 ID
```

### 조건문

```bash
# 기본 if-else
if [ "$NAME" == "admin" ]; then
    echo "관리자입니다"
elif [ "$NAME" == "guest" ]; then
    echo "게스트입니다"
else
    echo "일반 사용자입니다"
fi

# 숫자 비교
if [ $AGE -gt 18 ]; then    # greater than
    echo "성인"
fi

# 비교 연산자
# 문자열: ==, !=
# 숫자: -eq, -ne, -lt, -le, -gt, -ge
# 파일: -f (파일 존재), -d (디렉토리 존재), -r (읽기 가능)

# 파일 존재 확인
if [ -f "/path/to/file" ]; then
    echo "파일 존재"
fi

# 논리 연산
if [ "$A" == "1" ] && [ "$B" == "2" ]; then
    echo "둘 다 참"
fi

if [ "$A" == "1" ] || [ "$B" == "2" ]; then
    echo "하나라도 참"
fi
```

### 반복문

```bash
# for 루프 - 리스트 순회
for item in apple banana cherry; do
    echo "과일: $item"
done

# for 루프 - 범위
for i in {1..5}; do
    echo "숫자: $i"
done

# for 루프 - 파일 순회
for file in *.txt; do
    echo "텍스트 파일: $file"
done

# while 루프
count=0
while [ $count -lt 5 ]; do
    echo "카운트: $count"
    count=$((count + 1))
done

# 파일 읽기
while IFS= read -r line; do
    echo "줄: $line"
done < input.txt
```

### 함수

```bash
# 함수 정의
greet() {
    local name=$1  # 지역 변수
    echo "Hello, $name!"
}

# 함수 호출
greet "World"

# 반환값 (종료 코드)
is_valid() {
    if [ -z "$1" ]; then
        return 1  # 실패
    fi
    return 0  # 성공
}

if is_valid "test"; then
    echo "유효함"
fi

# 출력을 반환값처럼 사용
get_date() {
    echo $(date +%Y-%m-%d)
}
today=$(get_date)
```

### 파이프라인과 리다이렉션

```bash
# 파이프라인: 출력을 다음 명령의 입력으로
cat file.txt | grep "error" | wc -l

# 리다이렉션
command > file.txt    # 출력을 파일로 (덮어쓰기)
command >> file.txt   # 출력을 파일로 (추가)
command 2> error.txt  # 에러를 파일로
command &> all.txt    # 출력+에러를 파일로
command < input.txt   # 파일을 입력으로

# 실용 예시
# 로그에서 에러만 추출하여 저장
grep "ERROR" app.log | sort | uniq > errors.txt
```

### 실용 스크립트 예시

#### 백업 스크립트

```bash
#!/bin/bash
# 매일 백업 스크립트

BACKUP_DIR="/backup"
SOURCE_DIR="/data"
DATE=$(date +%Y%m%d)
BACKUP_FILE="$BACKUP_DIR/backup_$DATE.tar.gz"

# 백업 디렉토리 확인
if [ ! -d "$BACKUP_DIR" ]; then
    mkdir -p "$BACKUP_DIR"
fi

# 압축 백업
tar -czf "$BACKUP_FILE" "$SOURCE_DIR"

# 7일 이상 된 백업 삭제
find "$BACKUP_DIR" -name "backup_*.tar.gz" -mtime +7 -delete

echo "백업 완료: $BACKUP_FILE"
```

#### 배포 스크립트

```bash
#!/bin/bash
# 배포 스크립트

set -e  # 에러 시 즉시 중단

APP_DIR="/app"
REPO_URL="https://github.com/user/repo.git"
BRANCH=${1:-main}  # 기본값: main

echo "🚀 배포 시작: $BRANCH 브랜치"

# 저장소 업데이트
cd "$APP_DIR"
git fetch origin
git checkout "$BRANCH"
git pull origin "$BRANCH"

# 의존성 설치
npm install

# 빌드
npm run build

# 서비스 재시작
sudo systemctl restart myapp

echo "✅ 배포 완료!"
```

#### 모니터링 스크립트

```bash
#!/bin/bash
# 디스크 사용량 모니터링

THRESHOLD=80
EMAIL="admin@example.com"

USAGE=$(df / | grep / | awk '{print $5}' | sed 's/%//')

if [ $USAGE -gt $THRESHOLD ]; then
    echo "⚠️ 디스크 사용량 경고: ${USAGE}%" | mail -s "Disk Alert" $EMAIL
fi
```

### Cron 스케줄링

```bash
# crontab 편집
crontab -e

# 형식: 분 시 일 월 요일 명령어
# ┌───────────── 분 (0-59)
# │ ┌───────────── 시 (0-23)
# │ │ ┌───────────── 일 (1-31)
# │ │ │ ┌───────────── 월 (1-12)
# │ │ │ │ ┌───────────── 요일 (0-7, 0과 7은 일요일)
# │ │ │ │ │
# * * * * * command

# 예시
0 0 * * * /scripts/backup.sh          # 매일 자정
*/5 * * * * /scripts/health-check.sh  # 5분마다
0 9 * * 1 /scripts/weekly-report.sh   # 매주 월요일 9시
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 가볍고 빠름 | 복잡한 로직에 부적합 |
| 시스템 명령어 직접 사용 | 가독성 낮음 |
| 별도 설치 불필요 | 크로스 플랫폼 어려움 |
| CI/CD 통합 용이 | 디버깅 어려움 |

## 면접 예상 질문

### Q: 쉘 스크립트에서 변수를 사용할 때 주의점은?

A: (1) **할당 시 공백 없이**: `VAR=value` (❌ `VAR = value`) (2) **큰따옴표 사용**: 변수에 공백 있을 수 있음 `"$VAR"` (3) **중괄호 명시**: 문자열 연결 시 `${VAR}_suffix` (4) **로컬 변수**: 함수 내에서 `local` 키워드 사용.

### Q: set -e의 역할은?

A: 스크립트 실행 중 어떤 명령어라도 0이 아닌 종료 코드를 반환하면 즉시 스크립트 중단. **장점**: 오류 무시 방지, 안전한 스크립트. **주의**: 의도적으로 실패할 수 있는 명령어는 `|| true` 추가. **추가 옵션**: `set -u` (미정의 변수 오류), `set -o pipefail` (파이프라인 오류 감지).

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [자동화란](./what-is-automation.md) | 선수 지식 | [1] 기초 |
| [CI/CD 자동화](./cicd.md) | 활용 | [3] 중급 |
| [Git Hooks](../git/git-hooks.md) | 활용 | [3] 중급 |

## 참고 자료

- [Bash Reference Manual](https://www.gnu.org/software/bash/manual/)
- [ShellCheck](https://www.shellcheck.net/) - 쉘 스크립트 린터
- [Explain Shell](https://explainshell.com/) - 명령어 설명
