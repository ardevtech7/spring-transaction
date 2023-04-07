package hello.springtx.order;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
class OrderServiceTest {
    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @DisplayName("주문 정상")
    @Test
    void complete() throws NotEnoughMoneyException {
        // Given
        Order order = new Order();
        order.setUsername("정상");

        // When
        orderService.order(order);

        // Then
        Order findOrder = orderRepository.findById(order.getId()).get();
        assertThat(findOrder.getPayStatus()).isEqualTo("완료");
    }

    @DisplayName("런타임 예외")
    @Test
    void runtimeException() throws NotEnoughMoneyException {
        // Given
        Order order = new Order();
        order.setUsername("예외");

        // When
        assertThatThrownBy(() -> orderService.order(order))
                .isInstanceOf(RuntimeException.class);

        // Then
        // 런타임 익셉션 - 롤백, 데이터가 삽입되지 않는다.
        // Optional 은 값이 있는지 없는지 확인 가능
        Optional<Order> orderOptional = orderRepository.findById(order.getId());
        assertThat(orderOptional.isEmpty()).isTrue();
    }

    @DisplayName("체크 예외")
    @Test
    void bizException() throws NotEnoughMoneyException {
        // Given
        Order order = new Order();
        order.setUsername("잔고부족");

        // When
        try {
            orderService.order(order);
        } catch (NotEnoughMoneyException e) {
            log.info("고객에게 잔고 부족을 알리고 별도의 계좌로 입금하도록 안내");
        }

        // Then
        // 체크 익센셥 - 커밋, 데이터가 있어야 함
        Order findOrder = orderRepository.findById(order.getId()).get();
        assertThat(findOrder.getPayStatus()).isEqualTo("대기");
    }
}