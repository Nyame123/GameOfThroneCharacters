package com.bismark.gameofthronecharacters.data_layer

import com.bismark.gameofthronecharacters.core.Either
import com.bismark.gameofthronecharacters.core.Failure

interface INetworkRequest {
    fun <R> onNetworkCall(call: ()-> R): Either<Failure,R> = try{
        Either.Right(call())
    }catch (exception: Exception){
        Either.Left(Failure.ServerConnectionError(exception))
    }

    suspend fun <R> onSuspendCall(call: suspend ()-> R): Either<Failure,R> = try {
        Either.Right(call())
    }catch (exception: Exception){
        Either.Left(Failure.ServerConnectionError(exception))
    }

}
