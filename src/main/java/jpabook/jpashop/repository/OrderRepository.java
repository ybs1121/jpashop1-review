package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAllByString(OrderSearch orderSearch) {


        return em.createQuery("select o from Order o join o.member m" +
                        " where o.status = :status " +
                        " and m.name like :name", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
                .setMaxResults(1000)
                .getResultList();
    }

    /**
     * JPA Criteria
     *
     * @param orderSearch
     * @return
     */

    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> member = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<Predicate>();

        // 주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }

        //회원검색
        if (orderSearch.getMemberName() != null) {
            Predicate name = cb.like(member.get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(criteria.toArray(new Predicate[criteria.size()]));
        TypedQuery<Order> orderTypedQuery = em.createQuery(cq).setMaxResults(1000);
        return orderTypedQuery.getResultList();

    }


    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery("select o from Order o "
                + "join fetch o.member m "
                + "join fetch o.delivery d", Order.class).getResultList();
    }

    public List<SimpleOrderQueryDto> findOrderDtos() {
        return em.createQuery("select new jpabook.jpashop.repository.SimpleOrderQueryDto(o.id, m.name,o.orderDate,o.status,d.address) from Order o "
                + "join  o.member m "
                + "join  o.delivery d", SimpleOrderQueryDto.class).getResultList();
    }
}
