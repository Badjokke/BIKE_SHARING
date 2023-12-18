# Stage 1: Development stage
FROM node AS build

WORKDIR /usr/app
COPY . /usr/app
RUN npm ci
RUN npm run build

# Stage 4: Production stage
FROM nginx:alpine AS prod
EXPOSE 80
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /usr/app/build /usr/share/nginx/html
ENTRYPOINT ["nginx", "-g", "daemon off;"]