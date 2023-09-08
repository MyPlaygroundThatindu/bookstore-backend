package lk.ijse.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lk.ijse.bookstore.entity.Category;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
    
}
