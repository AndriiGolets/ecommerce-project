package site.golets.springbootecommerce.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "address")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Order order;

    private String street;

    private String city;

    private String state;

    private String country;

    private String zipCode;

}
