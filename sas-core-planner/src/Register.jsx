import React from 'react';
import { createRoot } from "react-dom/client";
import { apiUrl } from './config.js';

class RegisterPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
            error: ''
        }
    }

    updateUsername = (event) => {
        this.setState({ 
            username: event.target.value,
            error: ''
        });
    }

    updatePassword = (event) => {
        this.setState({ 
            password: event.target.value,
            error: ''
        });
    }

    signUp = () => {
        const { username, password } = this.state;
        if (username && password) {
            fetch(`${apiUrl}/auth/register`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username, password })
            })
            .then(response => {
                if (response.ok) {
                    window.location.href = 'sas-core-planner/login.html';
                } else {
                    this.setState({ 
                        username: '',
                        password: '',
                        error: 'Username already exists.' 
                    });
                }
            })
            .catch(error => {
                console.error('Error:', error);
                this.setState({ 
                    username: '',
                    password: '',
                    error: 'An error occurred while trying to register.' 
                });
            });
        } else {
            this.setState({ 
                username: '',
                password: '',
                error: 'Please enter a username and password.' 
            });
        }
    }

    render() {
        return (
            <div>
                <h1>Sign Up</h1>
                <form>
                    <label htmlFor="username">Username: </label>
                    <input type="text" id="username" name="username" value={this.state.username} onChange={this.updateUsername} required/>
                    <br />
                    <label htmlFor="password">Password: </label>
                    <input type="password" id="password" name="password" value={this.state.password} onChange={this.updatePassword} required />
                    <br />
                    <div>
                        <button id="register" type="button" onClick={this.signUp}>Sign Up</button>
                        <p id="error" style={{ color: 'red' }}>{this.state.error}</p>
                    </div>
                </form>
                <p>Already have an account? <a href="login.html">Login here</a>.</p>
            </div>
        );
    }
}

const root = createRoot(document.getElementById('root'));
root.render(<RegisterPage />);