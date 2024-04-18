package jpaboo.jpashop.service;

import jakarta.persistence.EntityManager;
import jpaboo.jpashop.domain.Member;
import jpaboo.jpashop.repository.MemberRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager em;


    @Test
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("KIM");
        //when
        Long saveId = memberService.join(member);
        //then
        em.flush();
        Assertions.assertEquals(member, memberRepository.findOne(saveId));

    }

    @Test
    public void 중복_회원_예외() throws Exception {

        //given
        Member member = new Member();
        member.setName("KIM");

        Member member2 = new Member();
        member2.setName("KIM");
        //when
        memberService.join(member);
        Assertions.assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2);
        });
    }

}