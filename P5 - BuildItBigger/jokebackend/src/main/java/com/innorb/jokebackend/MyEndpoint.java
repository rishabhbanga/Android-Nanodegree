/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.innorb.jokebackend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.innorb.TheJoker;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "jokebackend.innorb.com",
                ownerName = "jokebackend.innorb.com",
                packagePath = ""
        )
)
public class MyEndpoint {
    @ApiMethod(name = "crackJoke")
    public MyBean crackJoke(){
        MyBean response = new MyBean();
        TheJoker joker = new TheJoker();
        response.setData(joker.crackJoke());
        return response;
    }
}
