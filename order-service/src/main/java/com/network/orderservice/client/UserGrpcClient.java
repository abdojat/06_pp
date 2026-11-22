
package com.network.orderservice.client;

import com.network.userservice.grpc.UserRequest;
import com.network.userservice.grpc.UserResponse;
import com.network.userservice.grpc.UserServiceGrpc;
import io.grpc.StatusRuntimeException;
import org.springframework.stereotype.Component;
import org.springframework.grpc.client.GrpcChannelFactory;

import java.util.concurrent.TimeUnit;

@Component
public class UserGrpcClient {

    private final UserServiceGrpc.UserServiceBlockingStub stub;

    public UserGrpcClient(GrpcChannelFactory channelFactory) {
        this.stub = UserServiceGrpc.newBlockingStub(
                channelFactory.createChannel("user-service"));
    }

    public UserResponse getUser(Long userId)
            throws StatusRuntimeException {

        UserRequest request = UserRequest.newBuilder()
                .setUserId(userId)
                .build();

        return stub.withDeadlineAfter(5, TimeUnit.SECONDS)
                .getUser(request);
    }
}
