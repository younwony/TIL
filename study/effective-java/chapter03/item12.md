# Item 12: toString을 항상 재정의하라

## 핵심 정리

Object의 기본 toString 메서드는 `클래스_이름@16진수로_표시한_해시코드`를 반환한다. 이는 우리가 원하는 정보가 아니다.

## toString의 일반 규약

"간결하면서 사람이 읽기 쉬운 형태의 유익한 정보를 반환해야 한다"

## 좋은 toString 예제

```java
public final class PhoneNumber {
    private final short areaCode, prefix, lineNum;

    /**
     * 이 전화번호의 문자열 표현을 반환한다.
     * 이 문자열은 "XXX-YYY-ZZZZ" 형태의 12글자로 구성된다.
     * XXX는 지역 코드, YYY는 프리픽스, ZZZZ는 가입자 번호다.
     * 각각의 대문자는 10진수 숫자 하나를 나타낸다.
     */
    @Override
    public String toString() {
        return String.format("%03d-%03d-%04d",
                areaCode, prefix, lineNum);
    }
}
```

## toString을 재정의해야 하는 이유

1. println, printf, 문자열 연결 연산자(+), assert 구문에 넘길 때 자동으로 호출된다
2. 디버거가 객체를 출력할 때도 사용된다
3. 작성한 객체를 참조하는 컴포넌트가 오류 메시지를 로깅할 때 자동으로 호출된다

## 포맷 문서화 여부

### 포맷을 명시하면
- 장점: 그 값 그대로 입출력에 사용하거나 CSV 파일처럼 사람이 읽을 수 있는 데이터 객체로 저장할 수도 있다
- 단점: 포맷을 한번 명시하면 평생 그 포맷에 얽매이게 된다

### 포맷을 명시하든 아니든
- toString이 반환한 값에 포함된 정보를 얻어올 수 있는 API를 제공하자
- 그렇지 않으면 이 정보가 필요한 프로그래머는 toString의 반환값을 파싱할 수밖에 없다

## toString을 재정의하지 않아도 되는 경우

- 정적 유틸리티 클래스
- 대부분의 열거 타입 (자바가 이미 완벽한 toString을 제공)

## 참고

- 원본 코드: [effectiveJava/chapter_3/item_12](https://github.com/younwony/effectiveJava/tree/main/src/main/java/dev/wony/effectivejava/chapter_3/item_12)
