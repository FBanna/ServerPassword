package fbanna.serverpassword.state;

import org.apache.logging.log4j.core.util.Assert;

import java.util.concurrent.CompletableFuture;

public class LoginState {

    private LoginStates state;
    private CompletableFuture<Boolean> future;
    //private DialogEventCallback callback;

    /// New Login state which starts at watched login
    public LoginState(){
        this.state = LoginStates.WATCHED_LOGIN;
        //this.callback = null;

        this.future = null;

    }

    public boolean isWaitingResponse() {
        return this.state == LoginStates.WAITING_RESPONSE;
    }

    public boolean isWatchedLogin() {
        return this.state == LoginStates.WATCHED_LOGIN;
    }



    public void setWaitingResponse(CompletableFuture<Boolean> future) {
        this.state = LoginStates.WAITING_RESPONSE;
        this.future = future;
    }


    public void onResponse(Boolean result) {

        Assert.requireNonEmpty(this.future);

        this.future.complete(result);

    }
}
