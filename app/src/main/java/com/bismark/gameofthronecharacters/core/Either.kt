package com.bismark.gameofthronecharacters.core

sealed class Either<out L, out R> {
    /** * Represents the left side of [Either] class which by convention is a "Failure". */
    data class Left<out L>(val a: L) : Either<L, Nothing>()

    /** * Represents the right side of [Either] class which by convention is a "Success". */
    data class Right<out R>(val b: R) : Either<Nothing, R>()

    val isRight get() = this is Right<R>
    val isLeft get() = this is Left<L>

    fun <L> left(a: L) = Left(a)
    fun <R> right(b: R) = Right(b)

    fun either(fnL: (L) -> Any, fnR: (R) -> Any): Any =
        when (this) {
            is Left -> fnL(a)
            is Right -> fnR(b)
        }
}

// Credits to Alex Hart -> https://proandroiddev.com/kotlins-nothing-type-946de7d464fb
// Composes 2 functions
fun <A, B, C> ((A) -> B).c(f: (B) -> C): (A) -> C = {
    f(this(it))
}

/**
 * Returns the right value or fallback object.
 *
 * @param fallback The object returned if this is [Either.Left].
 */
fun <A, B> Either<A, B>.rightOrFallback(fallback: B): B = if (this is Either.Right) b else fallback

/**
 * Binds the given function across [Either.Right].
 *
 * @param f The function to bind across [Either.Right].
 */
inline fun <A, B, C> Either<A, B>.flatMap(f: (B) -> Either<A, C>): Either<A, C> =
    when (this) {
        is Either.Right -> f(this.b)
        is Either.Left -> this
    }

fun <T, L, R> Either<L, R>.map(fn: (R) -> (T)): Either<L, T> = this.flatMap(fn.c(::right))

/**
 * Convenient method for mapping types of [Either] with a [right][Either.Right] of type [List].
 *
 * Maps all items of the [List] and returns the [Either] with the mapped result.
 *
 * @param fn transform function that applies to each list item
 *
 * @return an [Either] with the mapped [List] as [Either.Right]
 */
fun <T, L, R> Either<L, List<R>>.mapItems(fn: (R) -> (T)): Either<L, List<T>> = this.map { list ->
    list.map(fn)
}

/**
 * Left-biased onFailure() FP convention dictates that when this class is Left, it'll perform
 * the onFailure functionality passed as a parameter, but, overall will still return an either
 * object so you chain calls.
 */
fun <L, R> Either<L, R>.onFailure(fn: (failure: L) -> Unit): Either<L, R> =
    this.apply { if (this is Either.Left) fn(a) }

/**
 * Left-biased onFailure() FP convention dictates that when this class is Left, it'll perform
 * the onFailure functionality passed as a parameter, but, overall will still return an either
 * object so you chain calls. [suspend version]
 */
suspend fun <L, R> Either<L, R>.onFailureSuspended(fn: suspend (failure: L) -> Unit): Either<L, R> =
    this.apply { if (this is Either.Left) fn(a) }

/**
 * Right-biased onSuccess() FP convention dictates that when this class is Right, it'll perform
 * the onSuccess functionality passed as a parameter, but, overall will still return an either
 * object so you chain calls.
 */
fun <L, R> Either<L, R>.onSuccess(fn: (success: R) -> Unit): Either<L, R> =
    this.apply { if (this is Either.Right) fn(b) }

/**
 * Right-biased onSuccess() FP convention dictates that when this class is Right, it'll perform
 * the onSuccess functionality passed as a parameter, but, overall will still return an either
 * object so you chain calls. [suspend version]
 */
suspend fun <L, R> Either<L, R>.onSuccessSuspended(fn: suspend (success: R) -> Unit): Either<L, R> =
    this.apply { if (this is Either.Right) fn(b) }

/**
 * like finally block in try/catch/finally
 */
fun <L, R> Either<L, R>.onEither(fn: () -> Unit): Either<L, R> =
    this.apply { fn() }

suspend fun <L, R, T> Either<L, R>.foldSuspendable(fnL: suspend (L) -> T?, fnR: suspend (R) -> T?): T? =
    when (this) {
        is Either.Left -> fnL(a)
        is Either.Right -> fnR(b)
    }

/**
 * Unwrap [Either] and return the right value or null if [Either] is left
 */
fun <T> Either<Any, T>.rightOrNull(): T? = when (this) {
    is Either.Right -> b
    is Either.Left -> null
}

/**
 * Unwraps [Either] and returns the right value or throws an exception if [Either] is left
 * @param e exception to throw (when left value)
 */
fun <T> Either<Any, T>.rightOrThrow(e: Throwable = Throwable(Exception())): T = when (this) {
    is Either.Right -> b
    is Either.Left -> throw e
}

/**
 * Converts an object to [Either.Right].
 */
fun <L, R> R.asEitherRight(): Either<L, R> {
    return Either.Right(this)
}

/**
 * Converts an object to [Either.Left].
 */
fun <L, R> L.asEitherLeft(): Either<L, R> {
    return Either.Left(this)
}
