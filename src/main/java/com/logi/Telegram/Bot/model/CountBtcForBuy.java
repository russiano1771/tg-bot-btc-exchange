package com.logi.Telegram.Bot.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "countBtc")
@Table(name = "countBtc")
@Data
@NoArgsConstructor
public class CountBtcForBuy {
    @Id
    private long id;
    private Integer countForBuy;




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getCountForBuy() {
        return countForBuy;
    }

    public void setCountForBuy(Integer countForBuy) {
        this.countForBuy = countForBuy;
    }

    @Override
    public String toString() {
        return "CountBtcForBuy{" +
                "id=" + id +
                ", countForBuy=" + countForBuy +
                '}';
    }
}
