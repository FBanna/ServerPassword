package fbanna.serverpassword.state;

import fbanna.serverpassword.event.DialogEventCallback;

import java.util.concurrent.CompletableFuture;

public class LoginState {

    private LoginStates state;
    //private CompletableFuture<Boolean> response;
    private DialogEventCallback callback;

    /// New Login state which starts at watched login
    public LoginState(){
        this.state = LoginStates.WATCHED_LOGIN;
        this.callback = null;
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

//    public void setWaitingResponse(CompletableFuture<Boolean> future) {
//        this.state = LoginStates.WAITING_RESPONSE;
//        this.response = future;
//    }

    public void setWaitingCallback(DialogEventCallback callback) {
        this.state = LoginStates.WAITING_RESPONSE;
        this.callback = callback;
    }

    public void onResponse(Boolean result) {
        //this.response.complete(result);
        this.callback.run(result);
    }
}
