package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.excepetion.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;


    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = createMemebr();
        Item book = createBook("책입니당1", 10, 10000);
        //when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        //then
        Order getOrder = orderRepository.findOne(orderId);
        Assertions.assertEquals(OrderStatus.ORDER, getOrder.getStatus());
        Assertions.assertEquals(1, getOrder.getOrderItems().size());
        Assertions.assertEquals(10000 * orderCount, getOrder.getTotalCount());
        Assertions.assertEquals(8, book.getStockQuantity());

    }



    @Test
    public void 상품주문_재고수량초과() throws Exception {
        //given
        Member member = createMemebr();
        Item book = createBook("책입니당1", 10, 10000);
        //when
        int orderCount = 11;

        //when

        //then
        Assertions.assertThrows(NotEnoughStockException.class, () -> {
            Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        });


    }

    @Test
    public void 주문취소() throws Exception {
        //given
        Member member = createMemebr();
        Item book1 = createBook("book1", 10, 10000);
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book1.getId(), orderCount);
        //when
        orderService.cancelOrder(orderId);
        //then
        Order getOrder = orderRepository.findOne(orderId);
        Assertions.assertEquals(10,book1.getStockQuantity());
        Assertions.assertEquals(OrderStatus.CANCEL,getOrder.getStatus());
    }


    private Item createBook(String name, int stockQuantity, int price) {
        Item book = new Book();
        book.setName(name);
        book.setStockQuantity(stockQuantity);
        book.setPrice(price);
        em.persist(book);
        return book;
    }

    private Member createMemebr() {
        Member member = new Member();
        member.setName("KIM");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }
}