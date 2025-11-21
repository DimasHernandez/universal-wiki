export interface DataMessage {
  message: string;
  typeMessage: TypeMessage;
}

type TypeMessage = 'success' | 'error' | 'warning';
