package com.michaelflisar.tests.tests

import android.util.Log
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

object RxMapTest {

    class House(val name: String, var rooms: List<Room>)
    class Room(val name: String)

    // Relays that hold all houses and all rooms
    private val storeRelayHouses: Relay<List<House>> = BehaviorRelay.create<List<House>>().toSerialized()
    private val storeRelayRooms: Relay<List<Room>> = BehaviorRelay.create<List<Room>>().toSerialized()

    fun testDelete() {
        // Test data: 1 house with 3 rooms
        val houses = listOf(House("House", ArrayList()))
        val rooms = listOf(Room("Living room"), Room("Bath"), Room("Bedroom"))
        houses[0].rooms = rooms

        // push data into relays
        storeRelayHouses.accept(houses)
        storeRelayRooms.accept(rooms)

        // test delete with dependencies + update the store relays!
        testDeleteHouse(houses[0])
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer {
                    Log.d("DB", "House AND dependencies deleted - ${Thread.currentThread()}")

                    // check stores
                    storeRelayHouses
                            .observeOn(Schedulers.io())
                            .subscribeOn(AndroidSchedulers.mainThread())
                            .subscribe(Consumer {
                                Log.d("DB", "Houses after deletion: ${it.size} - ${Thread.currentThread()}")
                            })

                    storeRelayRooms
                            .observeOn(Schedulers.io())
                            .subscribeOn(AndroidSchedulers.mainThread())
                            .subscribe(Consumer {
                                Log.d("DB", "Rooms after deletion: ${it.size} - ${Thread.currentThread()}")
                            })
                })
    }

    fun testDeleteHouse(item: House): Single<List<House>> {
        return storeRelayHouses
                .take(1)
                .flatMapIterable { it }
                .filter { it.equals(item) }
                .map {
                    Log.d("DB", "Begin Transaction (House) - ${Thread.currentThread()}")
                    it
                }
                .flatMap { Observable.fromIterable(it.rooms) }
                .flatMapSingle { testDeleteRoom(it) }
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
                .flatMap {
                    storeRelayHouses
                            .take(1)
                            .flatMapIterable { it }
                            .filter { !it.equals(item) }
                            .toList()
                            .map {
                                Log.d("DB", "New house list created: ${it.size} - ${Thread.currentThread()}")
                                it
                            }
                            .map {
                                storeRelayHouses.accept(it)
                                Log.d("DB", "New house list put back into store: ${it.size} - ${Thread.currentThread()}")
                                it
                            }
                }
    }

    fun testDeleteRoom(item: Room): Single<List<Room>> {
        return storeRelayRooms
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
                .flatMap {
                    storeRelayRooms
                            .take(1)
                            .flatMapIterable { it }
                            .filter { !it.equals(item) }
                            .toList()
                            .map {
                                Log.d("DB", "New room list created: ${it.size} - ${Thread.currentThread()}")
                                it
                            }
                            .map {
                                storeRelayRooms.accept(it)
                                Log.d("DB", "New room list put back into store: ${it.size} - ${Thread.currentThread()}")
                                it
                            }
                }
    }
}