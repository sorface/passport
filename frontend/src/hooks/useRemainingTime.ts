import {useEffect, useState} from 'react';

const updateIntervalMs = 1000;

export const useRemainingTime = (endDate: Date | null) => {
    const [remainingTimeMs, setRemainingTimeMs] = useState(0);

    useEffect(() => {
        if (!endDate) {
            return;
        }

        const updateTimer = () => {
            setRemainingTimeMs(endDate.getTime() - Date.now());
        };
        updateTimer();
        const intervalId = setInterval(updateTimer, updateIntervalMs);

        return () => {
            clearInterval(intervalId);
        };
    }, [endDate]);

    return {remainingTimeMs};
};
