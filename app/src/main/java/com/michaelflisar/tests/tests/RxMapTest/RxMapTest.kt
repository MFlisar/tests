package com.michaelflisar.tests.tests.RxMapTest

import android.util.Log
import com.jakewharton.rx.replayingShare
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
    private val relayHouses: Relay<List<House>> = BehaviorRelay.create<List<House>>().toSerialized()
    private val relayRooms: Relay<List<Room>> = BehaviorRelay.create<List<Room>>().toSerialized()

    // Cached intermediate results
    private val mapRooms : HashMap<House, Observable<List<Room>>> = HashMap()

    // maybe the caching is making a difference???
    private val USE_CACHE = true

    fun testDelete() {
        // Test data: 1 house with 3 rooms, 1 house without rooms
        val rooms = listOf(Room("Living room"), Room("Bath"), Room("Bedroom"))
        val houses = listOf(House("House 1", rooms), House("House 2", ArrayList()))

        // push data into relays
        relayHouses.accept(houses)
        relayRooms.accept(rooms)

        // test: delete first house (the one with 3 rooms) incl. dependencies + update the store relays!
        testDeleteHouse(houses[0])
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer {
                    Log.d("DB", "House AND dependencies deleted - ${Thread.currentThread()}")

                    // check stores
                    relayHouses
                            .take(1)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(Consumer {
                                Log.d("DB", "Houses after deletion: ${it.size} - ${Thread.currentThread()}")
                            })

                    relayRooms
                            .take(1)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(Consumer {
                                Log.d("DB", "Rooms after deletion: ${it.size} - ${Thread.currentThread()}")
                            })
                })
    }

    fun testDeleteHouse(item: House): Single<List<House>> {
        return relayHouses
                .take(1)
                .flatMapIterable { it }
                .filter { it.equals(item) }
                .map {
                    Log.d("DB", "Begin Transaction (House) - ${Thread.currentThread()}")
                    it
                }
                .flatMap { Observable.fromIterable(it.rooms) }
                .flatMapSingle { testDeleteRoom(item, it) }
                .toList()
                .map {
                    Thread.sleep(500)
                    Log.d("DB", "Deleting house (${item.name}) - ${Thread.currentThread()}")
                    it
                }
                .map {
                    Log.d("DB", "End Transaction (House) - ${Thread.currentThread()}")
                    it
                }
                .map { item }
                .flatMap {
                    relayHouses
                            .take(1)
                            .flatMapIterable { it }
                            .filter { !it.equals(item) }
                            .toList()
                            .map {
                                Log.d("DB", "New house list created: ${it.size} - ${Thread.currentThread()}")
                                it
                            }
                            .map {
                                relayHouses.accept(it)
                                Log.d("DB", "New house list put back into store: ${it.size} - ${Thread.currentThread()}")
                                it
                            }
                }
    }

    fun testDeleteRoom(house: House, item: Room): Single<List<Room>> {
        // simplified for testing, this should be like a intermediate result cache backed up by the store
        var roomObservable: Observable<List<Room>> = relayRooms
        if (USE_CACHE && mapRooms[house] == null) {
            mapRooms[house] = relayRooms
                    .flatMapSingle {
                        Observable.fromIterable(it)
                                .filter { house.rooms.contains(it) }
                                .toList()
                    }
                    .replayingShare()
            roomObservable = mapRooms[house]!!
        }
        return roomObservable
                .take(1)
                .flatMapIterable { it }
                .filter { it.equals(item) }
                .map {
                    Log.d("DB", "Begin Transaction (Room) - ${Thread.currentThread()}")
                    it
                }
                .map {
                    Thread.sleep(500)
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
                    relayRooms
                            .take(1)
                            .flatMapIterable { it }
                            .filter { !it.equals(item) }
                            .toList()
                            .map {
                                Log.d("DB", "New room list created: ${it.size} - ${Thread.currentThread()}")
                                it
                            }
                            .map {
                                relayRooms.accept(it)
                                if (USE_CACHE) {
                                    // new roomws so we need to clear intermediate results!
                                    mapRooms.remove(house)
                                }
                                Log.d("DB", "New room list put back into store: ${it.size} - ${Thread.currentThread()}")
                                it
                            }
                }
    }
}