package matjibback.repository;

import matjibback.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository  extends JpaRepository<Bookmark, String> {
    List<Bookmark> findBookmarkByMemId(int id);
    void deleteBookmarkByResIdAndMemId(int resId, int memId);
}
