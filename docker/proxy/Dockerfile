FROM node:16-alpine3.16 as sso-front

WORKDIR /source

ARG VITE_BACKEND_URL
ARG REACT_APP_WS_URL
ARG VITE_BUILD_HASH

ENV VITE_BACKEND_URL ${VITE_BACKEND_URL}
ENV REACT_APP_WS_URL ${REACT_APP_WS_URL}
ENV VITE_BUILD_HASH ${VITE_BUILD_HASH}

COPY client/ /source

RUN npm ci

RUN npm run build

FROM nginx:alpine

COPY --from=sso-front /source/build  /usr/share/nginx/html

RUN rm /etc/nginx/conf.d/default.conf

COPY docker/proxy/nginx.conf /etc/nginx/conf.d
COPY docker/proxy/mime.conf /etc/nginx/mime.types

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]