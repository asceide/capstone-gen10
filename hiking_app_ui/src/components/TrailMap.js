import {useState, useMemo, useCallback, useRef, useEffect} from 'react';
import {
    GoogleMap,
    Marker,
    Polyline
} from '@react-google-maps/api';


export default function TrailMap({trailMarkers, onMapClicked}) {
    const mapRef = useRef();
    let center = useMemo(() => ({lat: 39.8283, lng: -98.5795}), []);
    const options = useMemo(() => ( {
        disableDefaultUI: true,
        clickableIcons: false
    }), []);

    const onLoad = useCallback(map => (mapRef.current = map), []);

    return (
        <div className="container">
            <div className="map">
            <GoogleMap 
                zoom={5} 
                center={center} 
                mapContainerClassName="map-container"
                options={options}
                onLoad={onLoad}
                onClick={onMapClicked}
            >
                {trailMarkers?.map((coords, index) => {
                    return <Marker key={index} position={coords} />
                })}
                {trailMarkers.length > 1 && 
                    <Polyline
                    path={trailMarkers}
                    strokeColor='0000ff'
                    strokeOpacity={0.8}
                    strokeWeight={6} />
                }
             
            </GoogleMap>
            </div>
        </div>
    )
}