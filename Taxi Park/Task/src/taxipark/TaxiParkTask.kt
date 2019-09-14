package taxipark

import kotlin.math.floor

/*********************************************

data class TaxiPark(
val allDrivers: Set<Driver>,
val allPassengers: Set<Passenger>,
val trips: List<Trip>)

data class Driver(val name: String)
data class Passenger(val name: String)

data class Trip(
val driver: Driver,
val passengers: Set<Passenger>,
// the trip duration in minutes
val duration: Int,
// the trip distance in km
val distance: Double,
// the percentage of discount (in 0.0..1.0 if not null)
val discount: Double? = null
) {
// the total cost of the trip
val cost: Double
get() = (1 - (discount ?: 0.0)) * (duration + distance)
}
 **********************************************/

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
        allDrivers.filter { iDriver -> trips.none { trip -> trip.driver == iDriver } }.toSet()

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
        allPassengers.filter { iPassenger -> (trips.count { iTrip -> iTrip.passengers.contains(iPassenger) } >= minTrips) }.toSet()

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
        allPassengers.filter { iPassenger -> (trips.count { iTrip -> iTrip.driver == driver && iTrip.passengers.contains(iPassenger) }) > 1 }.toSet()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> {
    return allPassengers.filter { iPassenger ->
        (trips.count { iTrip -> iPassenger in iTrip.passengers && iTrip.discount ?: 0.0 != 0.0 } >
                trips.count { iTrip -> iPassenger in iTrip.passengers && iTrip.discount ?: 0.0 == 0.0 })
    }.toSet()
}

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? =
        trips.groupBy { (it.duration - it.duration % 10)..(it.duration + 9 - it.duration % 10) }
                .maxBy { iEntry -> iEntry.value.size }?.key

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (trips.isEmpty())
        return false

    val totalCost = trips.sumByDouble { it.cost }
    val numof20PDrivers: Int = floor(allDrivers.size * .2).toInt()

    var mapByDriver = trips.groupBy { it.driver }
    var driverTotalEarningList = mapByDriver.mapValues { (_, iTrip) -> iTrip.sumByDouble { it.cost } }.toList()
    var sortedList = driverTotalEarningList.sortedByDescending { it.second }
    var costOfTop80 = sortedList.take(numof20PDrivers).sumByDouble { it.second }

    println(totalCost)
    println(numof20PDrivers)

    return costOfTop80 >= totalCost * 0.8
}