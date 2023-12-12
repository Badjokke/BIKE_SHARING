import Stomp from 'stompjs';
import { Client, Message } from '@stomp/stompjs';
let client: Stomp.Client | null = null;

export interface SocketConnectionParams {
    url: string,
    connectCallback: (frame: Stomp.Frame | undefined) => any,
    errorCallback: (error: string | Stomp.Frame) => any

}
const createConnection = (args: SocketConnectionParams ): Client =>{
    /*client = Stomp.overWS(`ws://${args.url}`);
    client.connect({},args.connectCallback,args.errorCallback);
    return client;*/
    const client = new Client({
        brokerURL:args.url
    });
    client.onConnect = (frame) =>{
        args.connectCallback(frame);
    }
    client.onStompError = (frame) =>{
        args.errorCallback(frame);
    }
    client.activate();

    return client;
}

const connectCallback = (frame:Stomp.Frame) => {
    if(client == null)return;
    console.log("Connected to STOMP server");
    const bikeId = 0;
    client.subscribe(`/bike_ride/location/${bikeId}`, subscribeCallback);

    setInterval(() => {
        if(client == null)return;
        client.send("/bike_sharing/bike", {}, JSON.stringify({ bikeId: bikeId }));
    }, 2500);
};

const subscribeCallback = (message:any) => {
    console.log(message);
    return message.body;
};

const errorCallback = (error:any) => {
  console.error(`Error: ${error.headers.message}`);
};


// Export any useful functions or values (optional)
export { createConnection};