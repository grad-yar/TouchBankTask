package com.yar.touchbanktask.util.caching;


// since realm doesn't understand extending children of RealmObject
// we have to implement this interface for each POJO we wish to persist
public interface TimeStampedData {

    long getTimeFetched();

    void updateTimeFetched();
}
