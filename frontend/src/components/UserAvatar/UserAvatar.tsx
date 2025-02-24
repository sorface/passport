import {FunctionComponent} from 'react';

import './UserAvatar.css';

interface UserAvatarProps {
    src?: string;
    caption?: string;
    nickname: string;
}

export const UserAvatar: FunctionComponent<UserAvatarProps> = ({
                                                                   src,
                                                                   nickname,
                                                                   caption,
                                                                   ...restProps
                                                               }) => {
    if (!src) {
        return (
            <div
                className='user-avatar user-avatar-plug'
                {...restProps}
            >
                {caption || nickname[0].toLocaleUpperCase()}
            </div>
        );
    }

    return (
        <img
            src={src}
            className='user-avatar'
            alt={`${nickname} avatar`}
            {...restProps}
        />
    );
};
