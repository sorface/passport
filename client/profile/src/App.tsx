import React, {useEffect} from 'react';
import {BrowserRouter} from 'react-router-dom';
import {AppRoutes} from './routes/AppRoutes';
import {REACT_APP_VERSION} from './config';
import {Menu} from './components/Menu/Menu';
import {Footer} from "./components/Footer/Footer";
import {Loader} from './components/Loader/Loader';
import {AuthContext} from './context/AuthContext';
import {useGetAccountApi} from './hooks/useGetAccount';
import {Captions} from './constants';

import './App.css';
import {useCsrfApi} from "./hooks/useGetCsrf";

function App() {
    const {csrfConfigState, loadCsrfConfig} = useCsrfApi();

    const {accountState, loadAccount} = useGetAccountApi();
    const {
        process: {
            loading,
            error,
        },
        account,
    } = accountState;

    const accountWillLoad = !account && !error;

    useEffect(() => {
        loadCsrfConfig();
    }, [loadCsrfConfig]);

    useEffect(() => {
        if (!csrfConfigState.csrfConfig) {
            return;
        }
        loadAccount();
    }, [csrfConfigState.csrfConfig, loadAccount]);

    if (loading || accountWillLoad) {
        return (
            <div className="App-container" data-testid="App-container">
                <div className="App">
                    <div className="App-content">
                        <p>{Captions.Loading}...</p>
                        <Loader/>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <BrowserRouter>
            <AuthContext.Provider value={{ account, loadAccount }}>
                <div className="App-container" data-testid="App-container">
                    <Menu/>
                    <div className="App">
                        <div className="App-content">
                            <AppRoutes/>
                        </div>
                        <Footer version={REACT_APP_VERSION}/>
                    </div>
                </div>
            </AuthContext.Provider>
        </BrowserRouter>
    );
}

export default App;
