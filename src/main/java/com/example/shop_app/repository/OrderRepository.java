package com.example.shop_app.repository;

import com.example.shop_app.domain.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findAllByMemberIdOrderByIdDesc(Long memberId);
}
