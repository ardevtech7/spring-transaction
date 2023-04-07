package hello.springtx.order;

// 체크 예외가 발생할 때, 롤
public class NotEnoughMoneyException extends Exception {
    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
