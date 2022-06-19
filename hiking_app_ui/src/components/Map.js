import {useState, useMemo, useCallback, useRef} from 'react';
import {
    GoogleMap,
    Marker,
    Polyline
} from '@react-google-maps/api';


export default function Map() {
    const [markers, setMarkers] = useState([]);
    const mapRef = useRef();
    const center = useMemo(() => ({lat: 43, lng: -80}), []);
    const options = useMemo(() => ( {
        disableDefaultUI: true,
        clickableIcons: false
    }), []);

    const onLoad = useCallback(map => (mapRef.current = map), []);

    const onMapClicked = (clickEvent) => {
        const updatedMarkers = [...markers];
        updatedMarkers.push({lat: clickEvent.latLng.lat(), lng: clickEvent.latLng.lng()});
        setMarkers(updatedMarkers);
    }

    return <div className="container">
        <div className="map">
            <GoogleMap 
                zoom={10} 
                center={center} 
                mapContainerClassName="map-container"
                options={options}
                onLoad={onLoad}
                onClick={onMapClicked}
            >
                {markers?.map((coords, index) => {
                    return <Marker key={index} position={coords} />
                })}
                {markers.length > 1 && 
                    <Polyline
                    path={markers}
                    strokeColor='0000ff'
                    strokeOpacity={0.8}
                    strokeWeight={6} />
                }
                
                
            </GoogleMap>
        </div>
    </div>
}