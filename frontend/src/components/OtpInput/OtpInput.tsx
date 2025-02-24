// https://github.com/devfolioco/react-otp-input
import React, {Fragment, FunctionComponent} from 'react';

import './OtpInput.css';

interface OTPInputProps {
    /** Value of the OTP input */
    value: string;
    /** Number of OTP inputs to be rendered */
    length: number;
    /** Whether the first input should be auto focused */
    shouldAutoFocus?: boolean;
    error?: boolean;
    /** Callback to be called when the OTP value changes */
    onChange: (otp: string) => void;
}

export const OTPInput: FunctionComponent<OTPInputProps> = ({
                                                               value,
                                                               length,
                                                               onChange,
                                                               error,
                                                               shouldAutoFocus = false,
                                                           }) => {
    const [activeInput, setActiveInput] = React.useState(0);
    const inputRefs = React.useRef<Array<HTMLInputElement | null>>([]);

    const getOTPValue = () => (value ? value.toString().split('') : []);

    React.useEffect(() => {
        inputRefs.current = inputRefs.current.slice(0, length);
    }, [length]);

    React.useEffect(() => {
        if (shouldAutoFocus) {
            inputRefs.current[0]?.focus();
        }
    }, [shouldAutoFocus]);


    const isInputValueValid = (value: string) => {
        const isTypeValid = !isNaN(Number(value));
        return isTypeValid && value.trim().length === 1;
    };

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const {value} = event.target;

        if (isInputValueValid(value)) {
            changeCodeAtFocus(value);
            focusInput(activeInput + 1);
        }
    };

    const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const {nativeEvent} = event;
        const value = event.target.value;

        if (!isInputValueValid(value)) {
            // Pasting from the native autofill suggestion on a mobile device can pass
            // the pasted string as one long input to one of the cells. This ensures
            // that we handle the full input and not just the first character.
            if (value.length === length) {
                const hasInvalidInput = value.split('').some((cellInput) => !isInputValueValid(cellInput));
                if (!hasInvalidInput) {
                    handleOTPChange(value.split(''));
                    focusInput(length - 1);
                }
            }

            // @ts-expect-error - This was added previously to handle and edge case
            // for dealing with keyCode "229 Unidentified" on Android. Check if this is
            // still needed.
            if (nativeEvent.data === null && nativeEvent.inputType === 'deleteContentBackward') {
                event.preventDefault();
                changeCodeAtFocus('');
                focusInput(activeInput - 1);
            }

            // Clear the input if it's not valid value because firefox allows
            // pasting non-numeric characters in a number type input
            event.target.value = '';
        }
    };

    const handleFocus = (event: React.FocusEvent<HTMLInputElement>) => (index: number) => {
        setActiveInput(index);
        event.target.select();
    };

    const handleBlur = () => {
        setActiveInput(activeInput - 1);
    };

    const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
        const otp = getOTPValue();
        if ([event.code, event.key].includes('Backspace')) {
            event.preventDefault();
            changeCodeAtFocus('');
            focusInput(activeInput - 1);
        } else if (event.code === 'Delete') {
            event.preventDefault();
            changeCodeAtFocus('');
        } else if (event.code === 'ArrowLeft') {
            event.preventDefault();
            focusInput(activeInput - 1);
        } else if (event.code === 'ArrowRight') {
            event.preventDefault();
            focusInput(activeInput + 1);
        }
            // React does not trigger onChange when the same value is entered
        // again. So we need to focus the next input manually in this case.
        else if (event.key === otp[activeInput]) {
            event.preventDefault();
            focusInput(activeInput + 1);
        } else if (
            event.code === 'Spacebar' ||
            event.code === 'Space' ||
            event.code === 'ArrowUp' ||
            event.code === 'ArrowDown'
        ) {
            event.preventDefault();
        }
    };

    const focusInput = (index: number) => {
        const activeInput = Math.max(Math.min(length - 1, index), 0);

        if (inputRefs.current[activeInput]) {
            inputRefs.current[activeInput]?.focus();
            setActiveInput(activeInput);
        }
    };

    const changeCodeAtFocus = (value: string) => {
        const otp = getOTPValue();
        otp[activeInput] = value[0];
        handleOTPChange(otp);
    };

    const handleOTPChange = (otp: Array<string>) => {
        const otpValue = otp.join('');
        onChange(otpValue);
    };

    const handlePaste = (event: React.ClipboardEvent<HTMLInputElement>) => {
        event.preventDefault();

        const otp = getOTPValue();
        let nextActiveInput = activeInput;

        // Get pastedData in an array of max size (num of inputs - current position)
        const pastedData = event.clipboardData
            .getData('text/plain')
            .slice(0, length - activeInput)
            .split('');

        // Prevent pasting if the clipboard data contains non-numeric values for number inputs
        if (pastedData.some((value) => isNaN(Number(value)))) {
            return;
        }

        // Paste data from focused input onwards
        for (let pos = 0; pos < length; ++pos) {
            if (pos >= activeInput && pastedData.length > 0) {
                otp[pos] = pastedData.shift() ?? '';
                nextActiveInput++;
            }
        }

        focusInput(nextActiveInput);
        handleOTPChange(otp);
    };

    return (
        <Fragment>
            {Array.from({length}, (_, index) => index).map((index) => (
                <React.Fragment key={index}>
                    <input
                        className={`otp-input ${error ? 'otp-input-error' : ''}`}
                        value={getOTPValue()[index] ?? ''}
                        ref={(element) => (inputRefs.current[index] = element)}
                        onChange={handleChange}
                        onFocus={(event) => handleFocus(event)(index)}
                        onBlur={handleBlur}
                        onKeyDown={handleKeyDown}
                        onPaste={handlePaste}
                        autoComplete={'off'}
                        aria-label={`Please enter OTP character ${index + 1}`}
                        type='number'
                        inputMode='numeric'
                        onInput={handleInputChange}
                    />
                </React.Fragment>
            ))}
        </Fragment>
    );
};
