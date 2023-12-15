import Stomp from 'stompjs';
import { Client} from '@stomp/stompjs';

export interface SocketConnectionParams {
    url: string,
    connectCallback: (frame: Stomp.Frame | undefined) => any,
    errorCallback: (error: string | Stomp.Frame) => any,
    disconnectCallback: (frame: Stomp.Frame) => any,
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
    client.onDisconnect=(frame) =>{
        args.disconnectCallback(frame);
    }
    client.activate();

    return client;
}
/*
const connectCallback = (frame:Frame) => {
    if(client == null)return;
    console.log("Connected to STOMP server");
    const bikeId = 0;
    client.subscribe(`/bike_ride/location/${bikeId}`, subscribeCallback);

    setInterval(() => {
        if(client == null)return;
        client.send("/bike_sharing/bike", {}, JSON.stringify({ bikeId: bikeId }));
    }, 2500);
};

const subscribeCallback = (message: Stomp.Message): string => {
    console.log(message);
    return message.body;
};
*/

// Export any useful functions or values (optional)
export { createConnection};