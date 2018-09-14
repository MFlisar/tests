package com.michaelflisar.tests.tests

import android.util.Log
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

object RxMapTest {

    class House(val name: String, var rooms: List<Room>)
    class Room(val name: String)

    fun testDelete() {
        // Test data: 1 house with 3 rooms
        val houses = listOf(House("House", ArrayList()))
        val rooms = listOf(Room("Living room"), Room("Bath"), Room("Bedroom"))
        houses[0].rooms = rooms

        // create shared test observables
        val houseObservable = Observable.just(houses).share()
        val roomObservable = Observable.just(rooms).share()

        // test delete with dependencies
        testDeleteHouse(houseObservable, roomObservable, houses[0])
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer {
                    Log.d("DB", "House AND dependencies deleted - ${Thread.currentThread()}")
                })
    }

    fun testDeleteHouse(houseObservable: Observable<List<House>>, roomObservable: Observable<List<Room>>, item: House): Single<House> {
        return houseObservable
                .take(1)
                .flatMapIterable { it }
                .filter { it.equals(item) }
                .map {
                    Log.d("DB", "Begin Transaction (House) - ${Thread.currentThread()}")
                    it
                }
                .flatMap { Observable.fromIterable(it.rooms) }
                .flatMapSingle { testDeleteRoom(roomObservable, it) }
                .toList()
                .map {
                    Log.d("DB", "Deleting house (${item.name}) - ${Thread.currentThread()}")
                    it
                }
                .map {
                    Log.d("DB", "End Transaction (House) - ${Thread.currentThread()}")
                    it
                }
                .map { item }
    }

    fun testDeleteRoom(roomObservable: Observable<List<Room>>, item: Room): Single<Room> {
        return roomObservable
                .take(1)
                .flatMapIterable { it }
                .filter { it.equals(item) }
                .map {
                    Log.d("DB", "Begin Transaction (Room) - ${Thread.currentThread()}")
                    it
                }
                .map {
                    Log.d("DB", "Deleting room (${it.name}) - ${Thread.currentThread()}")
                    it
                }
                .map {
                    Log.d("DB", "End Transaction (Room) - ${Thread.currentThread()}")
                    it
                }
                .map { item }
                .singleOrError()
    }
}