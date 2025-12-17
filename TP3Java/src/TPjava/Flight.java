package TPjava;

import java.time.LocalDateTime;

public class Flight {

    private String airLineCode;
    private String airlineName;
    private LocalDateTime arrivalTime;
    private LocalDateTime departureTime;
    private String flightIata;
    private int number;

    private final Aeroport departure;
    private final Aeroport arrival;

    public Flight(Aeroport departure, Aeroport arrival) {
        this.departure = departure;
        this.arrival = arrival;
    }

    // Getters (adapte si ton UML impose d'autres noms)
    public Aeroport getDeparture() { return departure; }
    public Aeroport getArrival() { return arrival; }

    public String getAirLineCode() { return airLineCode; }
    public String getAirlineName() { return airlineName; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public String getFlightIata() { return flightIata; }
    public int getNumber() { return number; }

    // Setters (pratiques pour JsonFlightFiller)
    public void setAirLineCode(String airLineCode) { this.airLineCode = airLineCode; }
    public void setAirlineName(String airlineName) { this.airlineName = airlineName; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }
    public void setFlightIata(String flightIata) { this.flightIata = flightIata; }
    public void setNumber(int number) { this.number = number; }

    @Override
    public String toString() {
        return "Flight{" +
                "flightIata='" + flightIata + '\'' +
                ", airlineName='" + airlineName + '\'' +
                ", dep=" + (departure != null ? departure.getIATA() : "null") +
                ", arr=" + (arrival != null ? arrival.getIATA() : "null") +
                ", depTime=" + departureTime +
                ", arrTime=" + arrivalTime +
                '}';
    }
}
