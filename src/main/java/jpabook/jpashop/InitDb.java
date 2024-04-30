package jpabook.jpashop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    private void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        public void dbInit1() {
            Member member = new Member();
            member.setName("userA");
            member.setAddress(new Address("seoul", "1", "1111"));
            Member member2 = new Member();
            member2.setName("userB");
            member2.setAddress(new Address("busan", "2", "11221"));
            em.persist(member);
            em.persist(member2);

            Book book = new Book();
            book.setName("JPA1");
            book.setPrice(10000);
            book.setStockQuantity(100);
            em.persist(book);
            Book book2 = new Book();
            book2.setName("JPA2");
            book2.setPrice(10000);
            book2.setStockQuantity(100);
            em.persist(book2);

            Book book3 = new Book();
            book3.setName("SPRING1");
            book3.setPrice(10000);
            book3.setStockQuantity(100);
            em.persist(book3);
            Book book4 = new Book();
            book4.setName("SPRING2");
            book4.setPrice(10000);
            book4.setStockQuantity(100);
            em.persist(book4);



            OrderItem orderItem = OrderItem.createOrderItem(book, 10000, 1);
            OrderItem orderItem1 = OrderItem.createOrderItem(book2, 20000, 2);
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem, orderItem1);
            em.persist(order);

            OrderItem orderItem3 = OrderItem.createOrderItem(book3, 10000, 1);
            OrderItem orderItem4 = OrderItem.createOrderItem(book4, 20000, 2);
            Delivery delivery2 = new Delivery();
            delivery2.setAddress(member2.getAddress());
            Order order1 = Order.createOrder(member2, delivery2, orderItem3, orderItem4);
            em.persist(order1);


        }
    }

}


