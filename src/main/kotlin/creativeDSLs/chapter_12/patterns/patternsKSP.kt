package creativeDSLs.chapter_12.patterns

import creativeDSLs.chapter_12.patterns.Continent.*

enum class Continent {
    Europe, Africa, Asia, NorthAmerica, SouthAmerica, Australia, Antarctica
}

@DataClassPattern
data class Country(val name: String, val capital: String, val continent: Continent, val millionPeople: Double)


fun main() {
    val country = Country(
        name = "Estonia",
        capital = "Tallinn",
        continent = Europe,
        millionPeople = 1.3
    )

    val countryType = match(country) {
        country(name = +"Netherlands") then {
            "A country that eats prime ministers"
        }
        country(continent = +Asia, millionPeople = gt(500.0)) then {
            "${country.name} is a large Asian country"
        }
        country(millionPeople = ge(10.0) and le(100.0)) then {
            "${country.name} is a medium sized country"
        }
        country(continent = +Europe, millionPeople = le(10.0)) then {
            "${country.name} is a small European country"
        }
        country(continent = +Antarctica) then {
            "Something is seriously wrong here"
        }
        otherwise { "A quite ordinary country" }
    }

    println(countryType)
}
