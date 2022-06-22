# Build the react app with node and npm
FROM node:18-alpine as builder
WORKDIR /home/app
COPY . .
RUN npm ci && npm cache clean --force
RUN npm run build

# Use nginx to serve the react app
FROM nginx:1.21.6-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=builder /home/app/build /usr/share/nginx/html

# Copy the initialization scrip to the Nginx init scripts directory
COPY 92-set-environment-vars.sh /docker-entrypoint.default
# Make it executable
RUN chmod +x /docker-entrypoint.d/92-set-environment-vars.sh

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
