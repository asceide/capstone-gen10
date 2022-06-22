cat << EOF > /usr/share/nginx/html/config.js
window.API_URL = "$API_URL";
window.BUCKET_URL = "$BUCKET_URL";
windows.MAPS_API_KEY = "$MAPS_API_KEY";
EOF
