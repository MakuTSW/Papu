package com.example.papu.core;

public enum OrderState {
    CREATED,
    PREPARING,
    DELIVERY,
    COMPLETED;

    public OrderState getNext() {
        if(this.equals(COMPLETED)) return COMPLETED;
        OrderState[] e = OrderState.values();
        int i = 0;
        while (e[i] != this) i++;
        return e[i+1];
    }

    public OrderState getPrev() {
        if(this.equals(CREATED)) return CREATED;
        OrderState[] e = OrderState.values();
        int i = 0;
        while (e[i] != this) i++;
        return e[i-1];
    }
}
