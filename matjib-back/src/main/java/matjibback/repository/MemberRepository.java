package matjibback.repository;

import matjibback.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
    Member findMembersByToken(String token);
    Member findMembersByNameAndBirthdayAndBirthyear(String name, String birthday, int birthyear);

    Member findMembersById(int id);

    void deleteMemberById(int id);
}
