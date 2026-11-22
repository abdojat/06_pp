
package com.network.userservice;

import com.network.userservice.grpc.UserRequest;
import com.network.userservice.grpc.UserResponse;
import com.network.userservice.grpc.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

import java.util.Map;

@GrpcService
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

    private static final Map<Long, String[]> USERS = Map.of(
            101L, new String[]{"Ahmed", "ahmed@example.com"},
            102L, new String[]{"Sara", "sara@example.com"},
            103L, new String[]{"Omar", "omar@example.com"}
    );

    @Override
    public void getUser(
            UserRequest request,
            StreamObserver<UserResponse> responseObserver) {

        long userId = request.getUserId();
        String[] user = USERS.get(userId);

        UserResponse.Builder response = UserResponse.newBuilder()
                .setUserId(userId);

        if (user != null) {
            response.setUserName(user[0])
                    .setEmail(user[1])
                    .setExists(true);

            System.out.println("User found: " + user[0]);

        } else {
            response.setExists(false);
            System.out.println("User not found: " + userId);
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }
}
