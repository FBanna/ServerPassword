package fbanna.serverpassword.state;

import java.util.concurrent.CompletableFuture;

public class LoginState {

    private LoginStates state;
    private CompletableFuture<Boolean> response;

    /// New Login state which starts at watched login
    public LoginState(){
        this.state = LoginStates.WATCHED_LOGIN;
        this.response = null;
    }

    public boolean isWaitingResponse() {
        return this.state == LoginStates.WAITING_RESPONSE;
    }

    public boolean isWatchedLogin() {
        return this.state == LoginStates.WATCHED_LOGIN;
    }

//    public void setState(LoginStates state) {
//        this.state = state;
//    }

    public void setWaitingResponse(CompletableFuture<Boolean> future) {
        this.state = LoginStates.WAITING_RESPONSE;
        this.response = future;
    }

    public void joinFuture(Boolean result) {
        this.response.complete(result);
    }
}
