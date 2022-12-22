package com.example.slimecards
/**
 * Object Card with the next attributes:
 * @param id Unique numeric identifier of a Card.
 * @param resourceId Numeric value that represents the drawable path to a Card's picture.
 * @param turnedUp Boolean that represents if a Card's state its facing up or down, true or false respectively.
 */
data class Card( val id : Int, val resourceId : Int, var turnedUp : Boolean = false )