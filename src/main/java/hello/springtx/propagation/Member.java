package hello.springtx.propagation;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Member {
    @Id
    @GeneratedValue
    private Long id;
    private String username;

    // JPA 는 기본 생성자 필수
    public Member() {
    }

    public Member(String username) {
        this.username = username;
    }
}
