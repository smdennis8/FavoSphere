import React, {useEffect, useState} from "react";
import { useLocation } from "react-router-dom";

const MaybeShowLeftPanel = ({ children }) => {

    const location = useLocation();
    const [showLeftPanel, setShowLeftPanel] = useState(false);

    useEffect(() => {
        if(location.pathname === '/' || location.pathname === '/create-account') {
            setShowLeftPanel(false);
        } else {
            setShowLeftPanel(true);
        }
    }, [location]);

    return (
        <div>{showLeftPanel && children}</div>
    )
}

export default MaybeShowLeftPanel;