package org.kwrench.classes

import org.kwrench.collections.createMapWith
import org.kwrench.strings.toSymbol
import org.kwrench.symbol.Symbol
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.jvm.javaType

/**
 * The one of this or the other type that can hold values of both types.
 * If neither can directly hold values of the other, returns Any.
 */
fun <A, B>Class<A>?.mostGeneralType(otherType: Class<B>?): Class<out Any?> {
    return if (this == null && otherType == null) Any::class.java
           else if (this == null) otherType!!
           else if (otherType == null) this
           else if (this == otherType) this
           else if (isAssignableFrom(otherType)) this
           else if (otherType.isAssignableFrom(this)) otherType
           else Any::class.java
}


/**
 * True if this class is a wrapped or primitive number type.
 */
fun <T> Class<T>.isNumberType(): Boolean {
    return Number::class.java.isAssignableFrom(this) ||
            this == Double::class.javaPrimitiveType ||
            this == Float::class.javaPrimitiveType ||
            this == Long::class.javaPrimitiveType ||
            this == Int::class.javaPrimitiveType ||
            this == Short::class.javaPrimitiveType ||
            this == Byte::class.javaPrimitiveType
}


/**
 * Converts some number type to another specified by a KClass.
 */
fun <T: Number>Number.toNumberOfType(type: KClass<out T>): T = this.toNumberOfType(type.java)

/**
 * Converts some number type to another specified by a Class.
 */
fun <T: Number>Number.toNumberOfType(type: Class<out T>): T {
    return when (type) {
        Byte::class.java -> this.toByte() as T
        Short::class.java -> this.toShort() as T
        Int::class.java -> this.toInt() as T
        Integer::class.java -> this.toInt() as T
        Long::class.java -> this.toLong() as T
        Float::class.java -> this.toFloat() as T
        Double::class.java -> this.toDouble() as T
        BigInteger::class.java -> BigInteger(this.toString()) as T
        BigDecimal::class.java -> BigDecimal(this.toString()) as T

        Byte::class -> this.toByte() as T
        Short::class -> this.toShort() as T
        Int::class -> this.toInt() as T
        Long::class -> this.toLong() as T
        Float::class -> this.toFloat() as T
        Double::class -> this.toDouble() as T

        else -> throw IllegalArgumentException("Expected a number type, but got '$type'")
    }
}

/**
 * Parses some string with a number to a number type specified by a KClass.
 * @throws NumberFormatException if the string is not a valid representation of a number of that type.
 */
fun <T: Number>String.toNumberOfType(type: KClass<out T>): T = this.toNumberOfType(type.java)

/**
 * Parses some string with a number to a number type specified by a Class.
 * @throws NumberFormatException if the string is not a valid representation of a number of that type.
 */
fun <T: Number>String.toNumberOfType(type: Class<out T>): T {
    return when (type) {
        Byte::class.java -> this.toByte() as T
        Short::class.java -> this.toShort() as T
        Int::class.java -> this.toInt() as T
        Long::class.java -> this.toLong() as T
        Float::class.java -> this.toFloat() as T
        Double::class.java -> this.toDouble() as T
        BigInteger::class.java -> BigInteger(this) as T
        BigDecimal::class.java -> BigDecimal(this) as T

        Byte::class -> this.toByte() as T
        Short::class -> this.toShort() as T
        Int::class -> this.toInt() as T
        Long::class -> this.toLong() as T
        Float::class -> this.toFloat() as T
        Double::class -> this.toDouble() as T

        else -> throw IllegalArgumentException("Expected a number type, but got '$type'")
    }
}

/**
 * Converts this and another number to the specified number type and adds the result together.
 */
fun <T: Number>Number.add(other: Number, type: KClass<out T>): T = this.add(other, type.java)

/**
 * Converts this and another number to the specified number type and adds the result together.
 */
fun <T: Number>Number.add(other: Number, type: Class<out T>): T {
    return when (type) {
        Byte::class.java -> (this.toByte() + other.toByte()) as T
        Short::class.java -> (this.toShort() + other.toShort()) as T
        Int::class.java -> (this.toInt() + other.toInt()) as T
        Long::class.java -> (this.toLong() + other.toLong()) as T
        Float::class.java -> (this.toFloat() + other.toFloat()) as T
        Double::class.java -> (this.toDouble() + other.toDouble()) as T
        BigInteger::class.java -> (BigInteger(this.toString()).add(BigInteger(other.toString())) ) as T
        BigDecimal::class.java -> (BigDecimal(this.toString()).add(BigDecimal(other.toString())) ) as T
        else -> throw IllegalArgumentException("Expected a number type, but got '$type'")
    }
}

/**
 * Converts this and another number to the specified number type and subtracts the results.
 */
fun <T: Number>Number.subtract(other: Number, type: KClass<out T>): T = this.subtract(other, type.java)

/**
 * Converts this and another number to the specified number type and subtracts the results.
 */
fun <T: Number>Number.subtract(other: Number, type: Class<out T>): T {
    return when (type) {
        Byte::class.java -> (this.toByte() - other.toByte()) as T
        Short::class.java -> (this.toShort() - other.toShort()) as T
        Int::class.java -> (this.toInt() - other.toInt()) as T
        Long::class.java -> (this.toLong() - other.toLong()) as T
        Float::class.java -> (this.toFloat() - other.toFloat()) as T
        Double::class.java -> (this.toDouble() - other.toDouble()) as T
        BigInteger::class.java -> (BigInteger(this.toString()).subtract(BigInteger(other.toString())) ) as T
        BigDecimal::class.java -> (BigDecimal(this.toString()).subtract(BigDecimal(other.toString())) ) as T
        else -> throw IllegalArgumentException("Expected a number type, but got '$type'")
    }
}

/**
 * Converts this and another number to the specified number type and multiplies the result together.
 */
fun <T: Number>Number.multiply(other: Number, type: KClass<out T>): T = this.multiply(other, type.java)

/**
 * Converts this and another number to the specified number type and multiplies the result together.
 */
fun <T: Number>Number.multiply(other: Number, type: Class<out T>): T {
    return when (type) {
        Byte::class.java -> (this.toByte() * other.toByte()) as T
        Short::class.java -> (this.toShort() * other.toShort()) as T
        Int::class.java -> (this.toInt() * other.toInt()) as T
        Long::class.java -> (this.toLong() * other.toLong()) as T
        Float::class.java -> (this.toFloat() * other.toFloat()) as T
        Double::class.java -> (this.toDouble() * other.toDouble()) as T
        BigInteger::class.java -> (BigInteger(this.toString()).multiply(BigInteger(other.toString())) ) as T
        BigDecimal::class.java -> (BigDecimal(this.toString()).multiply(BigDecimal(other.toString())) ) as T
        else -> throw IllegalArgumentException("Expected a number type, but got '$type'")
    }
}

/**
 * Converts this and another number to the specified number type and divides the results.
 */
fun <T: Number>Number.divide(other: Number, type: KClass<out T>): T = this.divide(other, type.java)

/**
 * Converts this and another number to the specified number type and divides the results.
 */
fun <T: Number>Number.divide(other: Number, type: Class<out T>): T {
    return when (type) {
        Byte::class.java -> (this.toByte() / other.toByte()) as T
        Short::class.java -> (this.toShort() / other.toShort()) as T
        Int::class.java -> (this.toInt() / other.toInt()) as T
        Long::class.java -> (this.toLong() / other.toLong()) as T
        Float::class.java -> (this.toFloat() / other.toFloat()) as T
        Double::class.java -> (this.toDouble() / other.toDouble()) as T
        BigInteger::class.java -> (BigInteger(this.toString()).divide(BigInteger(other.toString())) ) as T
        BigDecimal::class.java -> (BigDecimal(this.toString()).divide(BigDecimal(other.toString())) ) as T
        else -> throw IllegalArgumentException("Expected a number type, but got '$type'")
    }
}

/**
 * Converts this and another number to the specified number type and performs a comparison
 */
fun <T: Number>Number.less(other: Number, type: Class<out T>): Boolean {
    return when (type) {
        Byte::class.java -> this.toByte() < other.toByte()
        Short::class.java -> this.toShort() < other.toShort()
        Int::class.java -> this.toInt() < other.toInt()
        Long::class.java -> this.toLong() < other.toLong()
        Float::class.java -> this.toFloat() < other.toFloat()
        Double::class.java -> this.toDouble() < other.toDouble()
        BigInteger::class.java -> (BigInteger(this.toString()).subtract(BigInteger(other.toString()))).signum() < 0
        BigDecimal::class.java -> (BigDecimal(this.toString()).subtract(BigDecimal(other.toString()))).signum() < 0
        else -> throw IllegalArgumentException("Expected a number type, but got '$type'")
    }
}
fun <T: Number>Number.lessOrEqual(other: Number, type: Class<out T>): Boolean = this == other || this.less(other, type)
fun <T: Number>Number.greater(other: Number, type: Class<out T>): Boolean = !this.lessOrEqual(other, type)
fun <T: Number>Number.greaterOrEqual(other: Number, type: Class<out T>): Boolean = !this.less(other, type)

fun KClass<out Number>.isIntegerType(): Boolean = this.java.isIntegerType()

fun Class<out Number>.isIntegerType(): Boolean {
    return this == Byte::class.java ||
            this == Short::class.java ||
            this == Int::class.java ||
            this == Long::class.java
}

fun KClass<out Number>.isFloatingPointType(): Boolean = this.java.isFloatingPointType()

fun Class<out Number>.isFloatingPointType(): Boolean {
    return this == Float::class.java ||
           this == Double::class.java
}


/**
 * @return the (unparametrized) class for the specified type, if possible, otherwise null.  Does not handle arrays.
 */
fun KType.javaClassOrNull(): Class<*>? = this.javaType.javaClassOrNull()


/**
 * @return the (unparametrized) class for the specified type, if possible, otherwise null.  Does not handle arrays.
 */
fun Type.javaClassOrNull(): Class<*>? {
    return when {
        this is Class<*> -> this
        this is WildcardType -> this.upperBounds[0].javaClassOrNull()
        this is ParameterizedType -> this.rawType.javaClassOrNull()
        else -> null
    }
}


/**
 * Tries to create a new instance of the specified class using a no-arguments constructor, returns null if it was not possible.
 */
fun <T: Any>Class<T>.newInstanceOrNull(): T? {
    return try {
        this.getDeclaredConstructor().newInstance()
    }
    catch (e: Throwable) {
        null
    }
}




/**
 * Either the parameter type is assignable to this type,
 * or this type and the parameter are a primitive and corresponding wrapped type or vice versa.
 *
 * Useful for testing whether two types are e.g. a primitive number and the corresponding wrapper, or the other way around.
 */
fun <T, S> Class<T>.isCompatibleType(fromType: Class<S>): Boolean {

    // Helper function
    fun <A: Any>eitherIsPrimitive(type: KClass<A>): Boolean {
        return type.javaPrimitiveType != null &&
                (this == type.javaObjectType && fromType == type.javaPrimitiveType) ||
                (this == type.javaPrimitiveType && fromType == type.javaObjectType)
    }

    return when {
        this.isAssignableFrom(fromType) -> true
        eitherIsPrimitive(Boolean::class) -> true
        eitherIsPrimitive(Byte::class) -> true
        eitherIsPrimitive(Short::class) -> true
        eitherIsPrimitive(Int::class) -> true
        eitherIsPrimitive(Long::class) -> true
        eitherIsPrimitive(Float::class) -> true
        eitherIsPrimitive(Double::class) -> true
        else -> false
    }
}




/**
 * Creates an instance of the class using the specified parameters as named constructor parameters.
 * Supplied parameters that do not match any parameter name in the constructor are silently ignored.
 * Searches the constructors of the class for the one that matches most of the names of the supplied parameters.
 * @throws IllegalArgumentException if the types of the parameters do not match up, or there is some other problem.
 */
fun <T: Any>KClass<T>.createInstance(parameters: Map<Symbol, Any?>): T {

    fun mapParamsForConstructor(constructor: KFunction<T>): Map<KParameter, Any?> {
        // Map parameters
        val mappedParams: MutableMap<KParameter, Any?> = HashMap()
        for (consParam in constructor.parameters) {
            val id = consParam.name?.toSymbol()
            if (id != null && parameters.contains(id)) {
                mappedParams.put(consParam, parameters.get(id))
            }
        }
        return mappedParams
    }

    // Determine constructor, by picking the one that has most matches
    val constructors = this.constructors.toList()
    val constructorsParams = constructors.map { mapParamsForConstructor(it) }
    if (constructorsParams.isEmpty()) throw IllegalArgumentException("No constructors found for class '${this.qualifiedName}'")
    var maxMatches = 0
    var maxMatchesIndex = 0
    for ((index, param) in constructorsParams.withIndex()) {
        if (param.size > maxMatches) {
            maxMatches = param.size
            maxMatchesIndex = index
        }
    }
    val bestMatchingConstructor = constructors[maxMatchesIndex]
    val constructorParameters = constructorsParams[maxMatchesIndex]

    // Invoke constructor with parameters
    return try {
        bestMatchingConstructor.callBy(constructorParameters)
    } catch (cause: Exception) {
        throw IllegalArgumentException(
            "Could not create an instance of $this " +
                    "\nusing the primary constructor with ${constructorParameters.size} parameters " +
                    "(${constructorParameters.map { "${it.key.name}: ${it.key.type}" }.joinToString(", ")}) " +
                    "\nusing ${parameters.size} parameters (${parameters.toString()}):\n" +
                    "${cause.message}", cause)
    }
}

/**
 * Creates an instance of the class using the specified parameters as sequential constructor parameters.
 * Searches the constructors of the class for the one that matches most of the types of the supplied parameters.
 * @throws IllegalArgumentException if the types of the parameters do not match up, or there is some other problem.
 */
fun <T: Any>KClass<T>.createInstance(parameters: List<Any?>): T {
    return this.createInstance(parameters.toTypedArray())
}

/**
 * Creates an instance of the class using the specified parameters as sequential constructor parameters.
 * Searches the constructors of the class for the one that matches most of the types of the supplied parameters.
 * @throws IllegalArgumentException if the types of the parameters do not match up, or there is some other problem.
 */
fun <T: Any>KClass<T>.createInstance(parameters: Array<Any?>): T {

    // Determine constructor, by picking the one that matches
    val constructors = this.constructors.toList()
    if (constructors.isEmpty()) throw IllegalArgumentException("No constructors found for class '${this.qualifiedName}'")

    fun findMatchingConstructor(): KFunction<T>? {
        for (constructor in constructors) {
            val constructorParams = constructor.parameters
            for ((index, constructorParameter) in constructorParams.withIndex()) {
                if (!constructorParameter.isOptional && index >= parameters.size) break
                if (index >= parameters.size) return constructor

                // Compare parameter type to required type
                var requiredParamType = constructorParameter.type.javaType
                val actualParamType = parameters[index]!!.javaClass

                if (requiredParamType is ParameterizedType) requiredParamType = requiredParamType.rawType // Handle case of a type with type parameters
                if (requiredParamType is Class<*> && !requiredParamType.isCompatibleType(actualParamType) ) {
                    break
                }
                // NOTE: For other special required types such as array types and such, we could do more checks, but the constructor will fail anyway in that case, so can as well let it try.

                // If we got to the end, we have a match
                if (index == parameters.size - 1) return constructor
            }
        }
        return null
    }

    val constructorToUse: KFunction<T>? = findMatchingConstructor()
    if (constructorToUse == null) throw IllegalArgumentException("No suitable constructor for the provided parameters (${parameters.joinToString { "${it?.javaClass?.name} ($it)" }}) found for class '${this.qualifiedName}'")

    val constructorParams = constructorToUse.parameters

    val mappedParams = constructorParams.createMapWith(parameters)

    fun createException(cause: Exception?, desc: String): IllegalArgumentException {
        return IllegalArgumentException(
            "Could not create an instance of $this " +
                    "\nusing the primary constructor with ${constructorParams.size} parameters " +
                    "(${constructorParams.joinToString(", ") { "${it.name}: ${it.type}" }}) " +
                    "\nusing ${parameters.size} parameters (${parameters.joinToString(", ")}):\n" +
                    "$desc ${cause?.message ?: ""}", cause)
    }

    if (parameters.size > constructorParams.size) throw (createException(null, "Too many parameters provided."))

    return try {
        constructorToUse.callBy(mappedParams)
    } catch (e: Exception) {
        throw createException(e, "Exception thrown by constructor:")
    }
}
