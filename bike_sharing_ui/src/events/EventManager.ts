export const fireDocumentEvent = (eventName: string, detail?:string)=>{
    const event = new CustomEvent(eventName, {
        detail: detail||'Custom event data',
      });
      document.dispatchEvent(event);
}