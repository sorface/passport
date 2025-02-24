import {FunctionComponent} from 'react';
import {padTime} from '../../utils/padTime';

const formatTime = (timeMs: number) => {
    const minutes = Math.floor((timeMs / 1000 / 60));
    const seconds = Math.floor((timeMs / 1000) % 60);
    return `${padTime(minutes)}:${padTime(seconds)}`;
};

interface RoomTimerProps {
    remainingTimeMs: number;
}

export const Timer: FunctionComponent<RoomTimerProps> = ({
                                                             remainingTimeMs,
                                                         }) => {
    return (
        <div className='timer'>
            {formatTime(remainingTimeMs <= 0 ? 0 : remainingTimeMs)}
        </div>
    );
};
