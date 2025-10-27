#FROM ubuntu:latest
#LABEL authors="USER"
#
#ENTRYPOINT ["top", "-b"]

FROM nginx:alpine

# Copy frontend files to nginx
COPY . /usr/share/nginx/html

# Copy nginx configuration
COPY nginx.conf /etc/nginx/nginx.conf

# Expose port 80
EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"]