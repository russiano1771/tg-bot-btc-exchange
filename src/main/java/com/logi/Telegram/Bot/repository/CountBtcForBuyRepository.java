package com.logi.Telegram.Bot.repository;

import com.logi.Telegram.Bot.model.CountBtcForBuy;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountBtcForBuyRepository extends JpaRepository<CountBtcForBuy, Long> {

    @Override
    Optional<CountBtcForBuy> findById(Long aLong);
}
