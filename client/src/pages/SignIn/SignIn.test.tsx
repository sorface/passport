import React from 'react';
import { render, screen } from '@testing-library/react';
import { SignIn } from './SignIn';
import { Captions } from '../../constants';

describe('Home', () => {
  test('renders app name', () => {
    render(<SignIn />);
    const welcomeElement = screen.getByRole('heading', {
      name: Captions.WelcomeToSSO,
    });
    expect(welcomeElement).toBeDefined();
  });
});
